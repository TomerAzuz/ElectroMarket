import React, { useContext, useEffect, useState } from "react";
import { SidebarContext } from "../contexts/SidebarContext";
import { CategoryContext } from "../contexts/CategoryContext";
import { CartContext } from "../contexts/CartContext";
import { FaShoppingCart } from 'react-icons/fa';
import { Link } from "react-router-dom";

function Header() {
  const [ isActive, setIsActive ] = useState(false);
  const { isOpenSidebar, setIsOpenSidebar } = useContext(SidebarContext);
  const { isOpenCategories, setIsOpenCategories } = useContext(CategoryContext);
  const { itemQuantity } = useContext(CartContext);

  useEffect(() => {
    window.addEventListener('scroll', () => {
      window.scrollY > 60 ? setIsActive(true) : setIsActive(false);
    });
  });

  return (
    <header className={`${
      isActive ? 'bg-blue-400 py-4 shadow-md' : 'bg-blue-400 py-6'} fixed flex justify-between items-center w-full p-10 z-10 transition-all`}>
      <Link to={'/'}>
        <div id="title" className="container mx-auto flex 
        items-center justify-between text-white text-4xl">
          ElectroMarket
        </div>
      </Link>
      <div
        className="cursor-pointer flex text-white text-2xl"
        onClick={() => setIsOpenCategories(!isOpenCategories)}
      >
        All Categories
      </div>

      <div
        className="cursor-pointer flex relative text-white max-w-[50px]"
        onClick={() => setIsOpenSidebar(!isOpenSidebar)}
      >
        <FaShoppingCart className="text-3xl" />
        <div className="bg-red-500 absolute -right-2 
        -bottom-2 text-[12px] w-[18px] h-[18px] 
        text-white rounded-full flex justify-center items-center">{itemQuantity}</div>
      </div>
    </header>
  );
}

export default Header;