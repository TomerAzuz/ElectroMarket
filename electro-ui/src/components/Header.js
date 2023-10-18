import React, { useContext, useState } from "react";
import { FaShoppingCart } from 'react-icons/fa';
import { Link } from "react-router-dom";

import { SidebarContext } from "../contexts/SidebarContext";
import { CategoryContext } from "../contexts/CategoryContext";
import { ProductContext } from '../contexts/ProductContext';
import { CartContext } from "../contexts/CartContext";

import Searchbar from "./Searchbar";
import SearchResults from "./SearchResults";

function Header() {
  const { products } = useContext(ProductContext);
  const { isOpenSidebar, setIsOpenSidebar } = useContext(SidebarContext);
  const { isOpenCategories, setIsOpenCategories } = useContext(CategoryContext);
  const { itemQuantity } = useContext(CartContext);
  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = (query) => {
    if (query.trim() !== '') {
      const filteredResults = products.filter(item => item.name.toLowerCase().startsWith(query.toLowerCase()));
      setSearchResults(filteredResults);
    } else {
      setSearchResults([]);
    }
  }

  return (
    <header 
    className='bg-red-500 py-4 shadow-md fixed flex justify-between items-center w-full p-10 z-10 transition-all'>
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
      <div className="relative flex-grow">
        <Searchbar onSearch={ handleSearch } />
        {searchResults.length > 0 && (
          <div className="absolute mt-4 left-0 w-full bg-white border rounded shadow-md">
            <SearchResults results={searchResults.slice(0, 5)} />
          </div>
        )}
      </div>
      <div
        className="cursor-pointer flex relative text-white max-w-[50px]"
        onClick={() => setIsOpenSidebar(!isOpenSidebar)}
      >
        <FaShoppingCart className="text-3xl" />
        <div className="bg-red-500 absolute -right-2 -bottom-2 text-[12px] w-[18px] h-[18px] text-white rounded-full flex justify-center items-center">{itemQuantity}</div>
      </div>
    </header>
  );
}

export default Header;
