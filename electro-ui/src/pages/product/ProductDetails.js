import React, { useContext } from 'react';
import { useLocation, useNavigate  } from 'react-router-dom';

import { CartContext } from '../../contexts/CartContext';

const ProductDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const product = location.state && location.state.product;
  const { addToCart } = useContext(CartContext);

  if (!product) {
    return (
      <section className='h-screen flex justify-center items-center'>
        Product not found
      </section>
    );
  }
  const { name, price, imageUrl } = product;

  return (
    <section className='py-48 flex items-center'>
      <div className='container mx-auto'>
        <div className='flex flex-col lg:flex-row items-center mb-8'>
          <div className='lg:flex-1 justify-center items-center mb-6'>
            <img
              className='w-full md:max-w-md lg:max-w-lg object-cover'
              src={imageUrl}
              alt={name}
            />
          </div>
          <div className='flex-2 text-center lg:text-left'>
            <button
              className='text-primary mb-4 cursor-pointer hover:underline'
              onClick={() => navigate(-1)}
            >
              Back
            </button>
            <h1 className='text-3xl lg:text-4xl xl:text-4xl 
                           font-medium mb-2 max-w-[450px] 
                           mx-auto'>
              {name}
            </h1>
            <div className='text-center text-3xl text-red-500 font-medium mb-6'>
              ${price.toLocaleString('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}
            </div>
            <div className='text-center lg:text-left mb-8'>
              <p className='mb-8'>Description...</p>
              <button
                onClick={() => addToCart(product)}
                className='cursor-pointer bg-red-500 rounded-md 
                        hover:bg-red-700 transition duration-300 
                          py-4 px-10 text-white font-semibold text-xl'
              >
                Add to cart
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default ProductDetails;