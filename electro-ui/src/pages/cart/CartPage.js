import React, { useContext, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiTrash2 } from 'react-icons/fi';

import { CartContext } from '../../contexts/CartContext';
import CartItem from '../../components/cart/CartItem';

const CartPage = () => {
  const { cart, clearCart, 
          total, itemQuantity, 
          handleCheckout, loading, 
          order, setOrder } = useContext(CartContext);

  const navigate = useNavigate();

  useEffect(() => {
    order && navigate('/order-confirmation', {
      state: {order: order}
    });
    setOrder(null);
  }, [order, navigate, setOrder]);
  
  const isCartEmpty = cart.length === 0;

  return (
    <div className="container mx-auto p-4 md:py-8 py-20 lg:py-16">
      <div>
        <h2 className="text-2xl font-semibold mb-4 mt-32">Shopping Cart</h2>
        {isCartEmpty ? 
          (
          <div className="text-center">
            <p className="text-lg font-semibold mb-4">Cart is empty</p>
            <Link to="/" className="bg-red-500 text-white p-3 rounded-md 
                                      text-lg font-semibold block">
              Go to Homepage
            </Link>
          </div>
          ) : (
          <>
            {loading && (
              <div className="text-center text-gray-600 mb-4">
                <p>Processing your order...</p>
              </div>
            )}
            <div className="mb-4">({itemQuantity} products)</div>
            <div className="grid grid-cols-1 gap-4">
              {cart.map((item) => (
                <CartItem key={item.id} item={item} />
              ))}
            </div>
            <div className="mt-4 flex flex-col md:flex-row items-center justify-between">
              <div className="text-2xl font-semibold mb-4 md:mb-0">
                Total: ${total.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </div>
              <div
                onClick={clearCart}
                className="cursor-pointer p-2 bg-red-500 text-white rounded-full text-xl"
              >
                <FiTrash2 />
              </div>
            </div>
            <div
              className={`cursor-pointer bg-primary flex justify-center items-center 
                         text-white p-4 rounded-md text-lg font-semibold mt-4 ${
                loading ? 'opacity-50 cursor-not-allowed' : ''}`}
              onClick={() => handleCheckout(3, 1000)}
              disabled={loading}
            >
              Checkout
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default CartPage;
