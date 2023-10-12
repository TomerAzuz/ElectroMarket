import React, { useContext } from "react";
import { Link } from 'react-router-dom';
import { IoMdArrowForward } from 'react-icons/io';
import { FiTrash2 } from 'react-icons/fi'; 

import CartItem from '../components/CartItem';
import { SidebarContext } from "../contexts/SidebarContext";

function Sidebar() {
  const { isOpenSidebar, handleCloseSidebar } = useContext(SidebarContext);

  return (
    <div
      className={`${
        isOpenSidebar ? 'right-0' : '-right-full'
      } w-full bg-white fixed top-0 h-full shadow-2xl md:w-[30vw] xl:max-w-[25vw] transition-all duration-300 z-20 px-4 lg:px-[30px]`}
    >
      <div className='flex items-center justify-between py-6 border-b'>
        <div className='uppercase text-sm font-semibold'>Shopping Bag (0)</div>
        <div onClick={handleCloseSidebar} className='cursor-pointer w-8 h-8 flex justify-center items-center'>
          <IoMdArrowForward className='text-2xl' />
        </div>
      </div>
    </div>
  );
}

export default Sidebar;