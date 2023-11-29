import React from 'react';
import { useLocation, Link } from 'react-router-dom';

const OrderConfirmation = () => {
  const location = useLocation();
  const order = location.state && location.state.order;
  const { id, total } = order;

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold mb-6 text-gray-800">Order Confirmed!</h1>
        <p className="text-lg text-gray-600 mb-4">
          Thank you for your order. <br />
          Your purchase has been confirmed, and we are processing it.
        </p>
        <div className="text-sm text-gray-500 mb-4">
          Order Details:
          <p className="text-base">
            Order number: {id} <br />
            Total: ${total.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} <br />
          </p>
        </div>
        <div className="mt-6">
          <Link
            to={'/user/orders'}
            className="bg-red-500 text-white px-6 py-3 rounded-md
                      hover:bg-red-600 focus:outline-none focus:ring
                      focus:border-red-300 block mb-2"
          >
            View Orders
          </Link>
          <Link
            to={'/'}
            className="bg-red-500 text-white px-6 py-3 rounded-md
                      hover:bg-red-600 focus:outline-none focus:ring
                      focus:border-red-300 block"
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    </div>
  );
};

export default OrderConfirmation;
