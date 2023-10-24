import React, { useContext, useState } from "react";
import { FaShoppingCart, FaBars } from 'react-icons/fa';
import { Link } from "react-router-dom";

import { CategoryContext } from "../contexts/CategoryContext";
import { ProductContext } from '../contexts/ProductContext';
import { CartContext } from "../contexts/CartContext";
import { UserContext } from "../contexts/UserContext";

import Searchbar from "./Searchbar";
import SearchResults from "./SearchResults";
import Account from "./Account";

function Header() {
  const { products } = useContext(ProductContext);
  const { user, setUser } = useContext(UserContext);
  const { isOpenCart, setIsOpenCart } = useContext(CartContext);
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
    <header className="bg-red-600 py-6 shadow-md fixed flex items-center justify-between
                        w-full p-10 z-10 transition-all">
      <Link to={'/'}>
        <div id="title" className="text-white text-5xl cursor-pointer">
          ElectroMarket
        </div>
      </Link>
      <div 
        className="cursor-pointer relative rounded-full border p-4 ml-4 lg:ml-10"
        onClick={() => setIsOpenCategories(!isOpenCategories)}>
      <div className="text-white text-3xl flex items-center">
      <FaBars className="mr-2" />
        All Categories
      </div>
    </div>
      <div className="relative flex-grow ml-40">
        <Searchbar onSearch={handleSearch} />
        {searchResults.length > 0 && (
          <div className="absolute mt-4 left-0 w-full bg-white border rounded shadow-md">
            <SearchResults results={searchResults.slice(0, 5)} />
          </div>
        )}
      </div>
      <div className="cursor-pointer flex text-black ml-6 group">
  <div className="text-3xl relative">
    <div className="rounded-full border w-12 h-12 flex justify-center items-center bg-white group-hover:bg-gray-300"
    onClick={() => setIsOpenCart(!isOpenCart)}>
      <FaShoppingCart />
    </div>
  </div>
  <div className="ml-2 text-xs w-7 h-7 bg-gray-100 text-black rounded-full flex justify-center items-center">
    {itemQuantity}
  </div>
  </div>
  <div className="cursor-pointer flex items-center text-black ml-6 group">
    <Account user={user}/>
  </div>
    </header>
  );
}  

export default Header;
