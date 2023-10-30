import React, { useContext, useState } from "react";
import { IoMdArrowForward } from 'react-icons/io';
import { FaShoppingCart } from 'react-icons/fa';
import { FiTrash2 } from "react-icons/fi";
import { Link } from 'react-router-dom';

import { CartContext } from "../../contexts/CartContext";
import CartItem from './CartItem';

function Cart() {
  const [isOpenCart, setIsOpenCart] = useState(false);
  const { cart, clearCart, total, itemQuantity, handleCheckout } = useContext(CartContext);
  
  return (
    <div>
      <div
        className="text-3xl flex relative rounded-full border w-14 h-14 justify-center items-center bg-white group-hover:bg-gray-300"
        onClick={() => setIsOpenCart(!isOpenCart)}
      >
        <FaShoppingCart className="text-3xl"/>
        <div className="bg-black absolute -right-2 -bottom-2 text-[12px] w-[18px] h-[18px] text-white rounded-full flex justify-center items-center">
            {itemQuantity}
        </div>
      </div>
      <div
         className={`${
          isOpenCart ? 'right-0' : '-right-full'} 
          w-full bg-white fixed top-0 h-full shadow-2xl md:w-[30vw] xl:max-w-[25vw] transition-all duration-300 z-20 px-4 lg:px-[30px]`}
      >
        <div className='flex items-center justify-between py-6 border-b'>
          <div className='uppercase text-sm font-semibold'>
            Shopping Cart ({itemQuantity})
          </div>
          <div
            onClick={() => setIsOpenCart(!isOpenCart)}
            className='cursor-pointer w-8 h-8 flex justify-center items-center'
          >
            <IoMdArrowForward className='text-2xl' />
          </div>
        </div>
        <div className="flex flex-col gap-y-2 h-[520px] lg-h-[640px] overflow-y-auto overflow-x-hidden border-b">
          {cart.map((item) => (
            <CartItem item={item} key={item.id} />
          ))}
        </div>
        <div className="flex flex-col gap-y-3 py-4 mt-4">
          <div className="flex w-full justify-between items-center">
            <div className="uppercase font-semibold">
              <span className="mr-2">Total:</span>$ {parseFloat(total).toFixed(2)}
            </div>
            <div
              onClick={clearCart}
              className="cursor-pointer py-4 bg-red-500 text-white w-12 h-12 flex justify-center items-center text-xl"
            >
              <FiTrash2 />
            </div>
          </div>
          <Link
            to='/cart'
            className="bg-gray-200 flex p-4 justify-center items-center text-primary w-full font-medium"
            onClick={() => setIsOpenCart(false)}
          >
            View Cart
          </Link>
          <Link
            to='/'
            className="bg-primary flex p-4 justify-center items-center text-white w-full font-medium"
            onClick={handleCheckout}
          >
            Checkout
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Cart;
