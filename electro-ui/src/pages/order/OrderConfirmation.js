import React from 'react';
import { Link } from 'react-router-dom';

const OrderConfirmation = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold mb-6 text-gray-800">Order Confirmed!</h1>
        <p className="text-lg text-gray-600 mb-4">
          Thank you for your order. <br />
          Your purchase has been confirmed, and we are processing it.
        </p>
        
        <div className="flex flex-col mt-6">
          <Link
            to={'/user/orders'}
            className="bg-red-500 text-white px-6 py-3 rounded-md
                      hover:bg-red-600 focus:outline-none focus:ring
                      focus:border-red-300 mb-2"
          >
            View Orders
          </Link>
          <Link
            to={'/'}
            className="bg-red-500 text-white px-6 py-3 rounded-md
                      hover:bg-red-600 focus:outline-none focus:ring
                      focus:border-red-300"
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    </div>
  );
};

export default OrderConfirmation;