import React, { createContext, useState, useEffect, useContext } from 'react';
import { toast } from 'react-hot-toast';

import { AuthContext } from './AuthContext';
import axiosInstance from '../axiosInterceptor'; 
import axios from 'axios';

export const CartContext = createContext();

const CartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [itemQuantity, setItemQuantity] = useState(0);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const { user } = useContext(AuthContext);
  const [order, setOrder] = useState(null);

  useEffect(() => {
    const totalPrice = cart.reduce((acc, item) => acc + item.price * item.quantity, 0);
    setTotal(totalPrice);

    const quantity = cart.reduce((acc, item) => acc + item.quantity, 0);
    setItemQuantity(quantity);
}, [cart]);

  const initiatePayment = async (total, orderId) => {
    try {
      const response = await axios.post('/createPayment', { total: total, orderId: orderId });

      if (response.status === 200)  {
        /* redirect to PayPal */
        window.location.href = response.data;
      }
    } catch (error) {
      console.error('Error initiating PayPal payment', error);
    }
  };

  const showNotification = (type, message) => {
    setLoading(false);
    toast[type](message);
  };

  const handleCheckout = async () => {
    const maxRetries = 3;
    let retries = 0;

    const buildOrderData = () => {
      return cart.map((item) => ({
        productId: item.id,
        quantity: item.quantity,
      }));
    };
  
  const submitOrder = async () => {
    setLoading(true);
    const orderData = buildOrderData();

    try {
      const response = await axiosInstance.post('/orders', orderData);
      
      if (response && response.status === 200) {
        setLoading(false);
        showNotification('success', 'Order Submitted Successfully');
        setOrder(response.data);
        console.log(response.data);
        clearCart();
        await initiatePayment(response.data.total, response.data.id);
      }
    } catch (error) {
        retries++;

        if (retries < maxRetries) {
          /* exponential backoff */
          const delay = Math.pow(2, retries) * 1000;
          setTimeout(submitOrder, delay);
        } else {
          showNotification('error', 'Unable to submit order.\n Please try again.');
        }
      }
  };

  if (!user) {
      showNotification('error', 'Please Sign In');
      return;
  }
  if (loading) {
      return;
  }
    try {
      await submitOrder();
    } catch (error) {
      showNotification('error', 'Failed to submit order');
    } 
  };

  const addToCart = (product) => {
    const newItem = {...product, quantity: 1};
    const cartItem = cart.find(item => {
      return item.id === product.id;
    });

    if (cartItem)   {
      const newCart = [...cart].map(item => {
        if (item.id === product.id) {
          if (item.quantity > 2)  {
            toast.error('You cannot order more than 3 items');
            return item;
          } else  {
            toast.success('Item added to cart');
            return {...item, quantity: cartItem.quantity + 1};
          }
        } 
        return item;
      });
      setCart(newCart);
    } else  {
      toast.success('Item added to cart');
      setCart([...cart, newItem]);
    }
  };

  const removeFromCart = (id) => {
    const newCart = cart.filter(item => {
      return item.id !== id;
    });
    setCart(newCart);
  };

  const clearCart = () => {
    setCart([]);
  };

  const increaseQuantity = (id) => {
    const item = cart.find(item => item.id === id);
    addToCart(item, id);
  };

  const decreaseQuantity = (id) => {
    const item = cart.find((cartItem) => cartItem.id === id);
    if (item) {
      let newCart;
      if (item.quantity > 1) {
        newCart = cart.map((cartItem) => {
          return cartItem.id === id ? 
            { ...cartItem, quantity: cartItem.quantity - 1 } : cartItem;
        });
      } else {
        newCart = cart.filter((cartItem) => cartItem.id !== id);
      }
      setCart(newCart);
    }
  };

  return <CartContext.Provider value={{ 
    cart,
    itemQuantity,
    total,
    loading,
    order,
    addToCart, 
    removeFromCart, 
    clearCart,
    increaseQuantity, 
    decreaseQuantity,
    handleCheckout,
    setOrder
  }}>{children}</CartContext.Provider>;
};

export default CartProvider;