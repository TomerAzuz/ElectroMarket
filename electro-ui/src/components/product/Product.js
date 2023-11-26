import React from 'react';
import { useNavigate } from 'react-router-dom';

const Product = ({ product }) => {
  const { name, imageUrl, price } = product;
  const navigate = useNavigate();

  const handleProductClick = () => {
    navigate(`/products/${product.name}`, {
      state: {product: product}
    });
  };
  
  return (
    <div
      className='cursor-pointer'
      onClick={handleProductClick}>
      <div 
        className='cursor-pointer border border-[#e4e4e4] 
                h-[300px] mb-4 relative overflow-hidden group transition'
      >
        <div className='w-full h-full flex justify-center items-center'>
          <div className='w-[200px] mx-auto flex justify-center items-center'>
            <img 
              className='max-h-[160px] transform transition duration-300 group-hover:scale-110' 
              src={imageUrl} 
              alt={name}
              loading="lazy"
            />
          </div>
        </div>
      </div>
      <h2 className="font-semibold text-lg mb-1 hover:underline">{name}</h2>
      <div className="font-semibold text-xl text-red-500"> 
        ${price.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</div>
    </div>
  );
};

export default Product;