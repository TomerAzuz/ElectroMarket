import React, { useContext } from 'react';
import { useLocation, useNavigate  } from 'react-router-dom';

import { CartContext } from '../contexts/CartContext';

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
    <section 
      className='py-60 mt-20 lg:py-16pt-8 
                 lg:pt-32 pb-12 lg:py-32
                 h-screen flex items-center'
      >
      <div className='container mx-auto'>
        <div 
          className='flex flex-col 
                     lg:flex-row items-center 
                     mb-8 lg:mb-0'>
          <div className='lg:flex-1 justify-center items-center'>
            <img
              className='w-full md:max-w-md lg:max-w-lg object-cover'
              src={imageUrl}
              alt={name}
            />
          </div>
          <div className='flex-2 text-center lg:text-left'>
            <button
              className='text-blue-500 mb-4 cursor-pointer'
              onClick={() => navigate(-1)}
            >
              Back
            </button>
            <h1 className='text-2xl lg:text-3xl 
                          xl:text-3xl font-medium 
                          mb-2 max-w-[450px] mx-auto lg:mx-0'>
              {name}
            </h1>
            <div className='text-2xl text-red-500 font-medium mb-6'>
              $ {price}
            </div>
            <div>
              <p className='mb-8'>Description...</p>
              <button
                onClick={() => addToCart(product)}
                className='bg-primary mb-28 py-4 px-8 text-white'
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
