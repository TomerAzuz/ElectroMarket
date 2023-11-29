import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { FaShoppingCart } from 'react-icons/fa';
import { toast } from 'react-hot-toast';

import { CartContext } from '../../contexts/CartContext';
import { AuthContext } from '../../contexts/AuthContext';

function Cart() {
  const { itemQuantity } = useContext(CartContext);
  const { user } = useContext(AuthContext);
  
  const handleCartClick = () => {
    if (!user)  {
      toast.error('Please Sign In');
    }
  }

  return (
    <div onClick={handleCartClick}>
      <Link to={user ? "/cart" : "/"}>
        <div className="mr-4 flex relative 
                        rounded-full border w-12 h-12 
                        justify-center items-center 
                        bg-white group hover:bg-gray-300">
            <FaShoppingCart className="text-3xl" />
          <div className="bg-black text-2xl absolute -right-2 -bottom-2 
                            text-[12px] w-[18px] h-[18px] text-white 
                            rounded-full flex justify-center items-center">
            {itemQuantity}
          </div>
        </div>
      </Link>
    </div>
  );
}

export default Cart;