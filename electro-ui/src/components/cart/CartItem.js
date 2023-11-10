import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { IoMdAdd, IoMdClose, IoMdRemove } from 'react-icons/io';
import { CartContext } from '../../contexts/CartContext';

const CartItem = ({ item }) => {
  const { id, name, price, imageUrl, quantity } = item;
  const { removeFromCart, increaseQuantity, decreaseQuantity } = useContext(CartContext);

  return (
    <div className='flex flex-col lg:flex-row gap-4 py-2 lg:px-6 border-b border-gray-200 font-light text-gray-500'>
      <div className='w-full lg:w-1/4 lg:min-h-[150px] flex items-center lg:flex-col lg:items-start gap-x-4'>
        <Link to={`products/${id}`}>
          <img
            className='h-auto max-w-[150px] lg:w-full'
            src={imageUrl}
            alt={name}
          />
        </Link>
      </div>
      <div className='w-full lg:w-3/4'>
        <div className='flex flex-col lg:flex-row justify-between items-center lg:items-start'>
          <Link to={`/products/${id}`} className='text-lg uppercase font-medium max-w-full lg:max-w-[300px] text-primary hover:underline'>
            {name}
          </Link>
          <div
            onClick={() => removeFromCart(id)}
            className='text-xl cursor-pointer text-gray-500 hover:text-red-500 transition mt-2 lg:mt-0'
          >
            <IoMdClose />
          </div>
        </div>
        <div className='flex gap-x-2 h-[36px] text-sm mt-2 lg:mt-4'>
          <div className='flex flex-1 max-w-full lg:max-w-[100px] items-center h-full border text-primary font-medium'>
            <div
              className='flex-1 h-full flex justify-center items-center cursor-pointer hover:bg-gray-200'
              onClick={() => decreaseQuantity(id)}
            >
              <IoMdRemove />
            </div>
            <div className='h-full flex justify-center items-center px-2'>
              {quantity}
            </div>
            <div
              className='flex-1 h-full flex justify-center items-center cursor-pointer hover:bg-gray-200'
              onClick={() => increaseQuantity(id)}
            >
              <IoMdAdd />
            </div>
          </div>
          <div className='text-lg flex-1 flex items-center justify-around'>$ {price}</div>
          <div className='text-lg flex-1 flex justify-end items-center text-primary font-medium'>
            {`$ ${parseFloat(price * quantity).toFixed(2)}`}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartItem;
