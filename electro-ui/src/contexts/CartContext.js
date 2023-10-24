import React, {createContext, useState, useEffect} from 'react';

export const CartContext = createContext();

const CartProvider = ({ children }) => {
    const [cart, setCart] = useState([]);
    const [itemQuantity, setItemQuantity] = useState(0);
    const [total, setTotal] = useState(0);
    const [isOpenCart, setIsOpenCart] = useState(false);

    const handleCloseCart = () => {
        setIsOpenCart(false);
    };

    // update total price
    useEffect(() => {
        const total = cart.reduce((acc, item) => {
            return acc + item.price * item.quantity;
        }, 0);
        setTotal(total);
    }, [cart]);

    // update item quantity
    useEffect(() => {
        if (cart)   {
            const quantity = cart.reduce((acc, item) => {
                return acc + item.quantity;
            }, 0);
            setItemQuantity(quantity);
        }
    }, [cart])

    const addToCart = (product) => {
        const newItem = {...product, quantity: 1}
        const cartItem = cart.find(item => {
            return item.id === product.id;
        });

        if (cartItem)   {
            const newCart = [...cart].map(item => {
                if (item.id === product.id) {
                    return {...item, quantity: cartItem.quantity + 1};
                } else  {
                    return item;
                }
            });
            setCart(newCart);
        } else  {
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
        const item = cart.find(cartItem => {
            return cartItem.id === id;
        });
        if (item)   {
            const newCart = cart.map(cartItem => {
                if (cartItem.id === id) {
                    return {...cartItem, quantity: item.quantity - 1}
                } else  {
                    return item;
                }
            });
            setCart(newCart);
        } 
        if (item.quantity < 2)    {
            removeFromCart(id);
        }  
    };

    return <CartContext.Provider value={{ isOpenCart, 
                                          setIsOpenCart, 
                                          handleCloseCart, 
                                          cart,
                                          addToCart, 
                                          removeFromCart, 
                                          clearCart,
                                          increaseQuantity, 
                                          decreaseQuantity,
                                          itemQuantity,
                                          total
                                       }}>{children}</CartContext.Provider>
};

export default CartProvider;