import React, { useContext } from "react";
import { SidebarContext } from "../contexts/SidebarContext";
import { CategoryContext } from "../contexts/CategoryContext";
import { FaShoppingCart } from 'react-icons/fa';

function Header() {
  const {isOpenSidebar, setIsOpenSidebar} = useContext(SidebarContext);
  const {isOpenCategories, setIsOpenCategories} = useContext(CategoryContext);
  
  return (
    <header className="bg-blue-500 flex justify-between items-center p-10">
      <div id="title" className="text-white text-4xl">ElectroMarket</div>

      <div
        className="cursor-pointer text-white text-2xl"
        onClick={() => setIsOpenCategories(!isOpenCategories)}
      >
        All Categories
      </div>

      <div
        className="cursor-pointer flex text-white"
        onClick={() => setIsOpenSidebar(!isOpenSidebar)}
      >
        <FaShoppingCart className="text-3xl" />
      </div>
    </header>
  );
}

export default Header;