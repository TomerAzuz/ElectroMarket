import React, { useContext } from 'react';
import { useLocation } from 'react-router-dom';

import { CartContext } from '../contexts/CartContext';

const ProductDetails = () => {
  const location = useLocation();
  const product = location.state && location.state.product;
  const { addToCart } = useContext(CartContext);

  if (!product) {
    return <section className='h-screen flex justify-center items-center'>Product not found</section>;
  }
  const { name, price, imageUrl } = product;

  return (
    <section className='pt-8 lg:pt-32 pb-12 lg:py-32 h-screen flex items-center'>
      <div className='container mx-auto'>
        <div className='flex flex-col lg:flex-row items-center mb-8 lg:mb-0'>
          <div className='lg:flex-1 justify-center items-center'>
            <img
              className='w-full lg:max-w-lg object-cover'
              src={imageUrl}
              alt={name}
            />
          </div>
          <div className='flex-1 text-center lg:text-left'>
            <h1 className='text-2xl lg:text-3xl xl:text-4xl font-medium mb-2 max-w-[450px] mx-auto lg:mx-0'>
              {name}
            </h1>
            <div className='text-lg lg:text-xl xl:text-2xl text-red-500 font-medium mb-6'>
              $ {price}
            </div>
            <div>
              <p className='mb-8'>Description...</p>
              <button
                onClick={() => addToCart(product)}
                className='bg-primary py-4 px-8 text-white'>
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
