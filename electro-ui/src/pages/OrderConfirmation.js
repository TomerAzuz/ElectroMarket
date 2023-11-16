import React from 'react';
import { useLocation, Link } from 'react-router-dom';

const OrderConfirmation = () => {
  const location = useLocation();
  const order = location.state && location.state.order;

  return (
    <div className="bg-gray-100 min-h-screen flex items-center justify-center">
      <div className="bg-white p-8 rounded-md shadow-md max-w-md">
        <h1 className="text-3xl font-semibold mb-6">Order Confirmed!</h1>
        <p className="text-gray-600 mb-4">
          Thank you for your order. <br/>
          Your purchase has been confirmed, and we are processing it.
        </p>
        <div className="border-t border-gray-300 pt-4">
          <p className="text-sm text-gray-500">
            Order Details:
          </p>
          Order number: {order.id} <br/>
          Total: $ {order.total.toFixed(2)} <br/>
        </div>
        <Link 
          to={'/'}
          className="mt-10 bg-red-500 text-white px-4 py-2 rounded-md 
                        hover:bg-red-600 focus:outline-none focus:ring 
                        focus:border-red-300">
          Continue Shopping
        </Link>
      </div>
    </div>
  );
};

export default OrderConfirmation;
