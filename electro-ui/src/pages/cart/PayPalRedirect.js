import React from 'react';
import Loader from '../../components/common/Loader';

const PayPalRedirect = () => {
  return (
    <div className='flex flex-col items-center justify-center h-screen text-black py-72'>
      <h2 className='text-4xl font-extrabold mb-4'>Hold on tight!</h2>
      <p className='text-lg mb-8'>We're redirecting you to PayPal...</p>
      <Loader loading={true} />
    </div>
  );
};

export default PayPalRedirect;
