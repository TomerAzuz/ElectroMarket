import React from 'react';
import { Link } from 'react-router-dom';

const ErrorPage = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold mb-6 text-gray-800">Oops! Page not found</h1>
        <p className="text-lg text-gray-600 mb-4">
        The page you are looking for might be in another universe.
        </p>
        <div className="mt-12">
          <Link
            to={'/'}
            className="bg-red-500 text-white px-6 py-3 rounded-md
                      hover:bg-red-600 focus:outline-none focus:ring
                      focus:border-red-300"
          >
            Back to homepage
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage;
