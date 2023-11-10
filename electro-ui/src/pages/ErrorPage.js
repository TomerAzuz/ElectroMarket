import React from 'react';

const ErrorPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white rounded-lg p-8 shadow-md">
        <h2 className="text-4xl font-bold text-red-600 mb-4">Error</h2>
        <p className="text-lg mb-4 font-bold">Page not found</p>
      </div>
    </div>
  );
};

export default ErrorPage;