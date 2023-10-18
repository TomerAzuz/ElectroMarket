import React, { useContext } from "react";
import { IoMdArrowForward } from 'react-icons/io';
import { SidebarContext } from "../contexts/SidebarContext";
import { CartContext } from "../contexts/CartContext";
import CartItem from './CartItem';
import { FiTrash2 } from "react-icons/fi";
import { Link } from 'react-router-dom';
import axios from 'axios'; 

function Sidebar() {
  const { isOpenSidebar, handleCloseSidebar } = useContext(SidebarContext);
  const { cart, clearCart, total, itemQuantity } = useContext(CartContext);

  const handleCheckout = async () => {
    const orderData = {
      items: cart.map(item => ({
        productId: item.id,
        quantity: item.quantity
      }))
    };
    console.log(orderData);
    try {
      const response = await axios.post("/orders", orderData);
      console.log("order submitted:", response.data);
      
      clearCart();      
    } catch (error) {
      console.log("Error submitting order:", error);
    }
  };

  return (
    <div
      className={`${
        isOpenSidebar ? 'right-0' : '-right-full'
      } w-full bg-white fixed top-0 h-full shadow-2xl md:w-[30vw] xl:max-w-[25vw] transition-all duration-300 z-20 px-4 lg:px-[30px]`}
    >
      <div className='flex items-center justify-between py-6 border-b'>
        <div className='uppercase text-sm font-semibold'>Shopping Bag ({itemQuantity})</div>
        <div onClick={handleCloseSidebar} className='cursor-pointer w-8 h-8 flex justify-center items-center'>
          <IoMdArrowForward className='text-2xl' />
        </div>
      </div>
      <div className="flex flex-col gap-y-2 h-[520px]
      lg:h-[640px] overflow-y-auto overflow-x-hidden border-b">
        {cart.map((item) => {
        return <CartItem item={item} key={ item.id } />;
        })}
      </div>
        <div className="flex flex-col gap-y-3 py-4 mt-4">
          <div className="flex w-full justify-between items-center">
            <div className="uppercase font-semibold">
              <span className="mr-2">Total:</span>$ {parseFloat(total).toFixed(2)}
            </div>
            <div onClick={clearCart} 
            className="cursor-pointer py-4 bg-red-500
            text-white w-12 h-12 flex justify-center
              items-center text-xl">
              <FiTrash2 />
            </div>
          </div>
          <Link to='/' className="bg-gray-200 flex p-4
          justify-center items-center text-primary w-full font-medium"
          >
            View Cart
          </Link>

          <Link to='/' className="bg-primary flex p-4
          justify-center items-center text-white w-full font-medium"
          onClick={handleCheckout}
          >
            Checkout
          </Link>
        </div>
    </div>

  );
}

export default Sidebar;