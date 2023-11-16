import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { FaShoppingCart } from 'react-icons/fa';

import { CartContext } from '../../contexts/CartContext';

function Cart() {
  const { itemQuantity } = useContext(CartContext);

  return (
    <Link to="/cart">
      <div className="mr-4 text-2xl flex relative rounded-full border w-12 h-12 
                      justify-center items-center bg-white group hover:bg-gray-300">
        
          <FaShoppingCart className="text-3xl" />
        <div className="bg-black absolute -right-2 -bottom-2 text-[12px] w-[18px] h-[18px] text-white rounded-full flex justify-center items-center">
          {itemQuantity}
        </div>
      </div>
    </Link>
  );
}

export default Cart;