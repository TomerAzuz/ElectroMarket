import React from 'react';
import { Link } from 'react-router-dom';
import { scrollToTop } from './utils';
import Searchbar from '../search/Searchbar';
import User from '../user/User';
import Cart from '../cart/Cart';

function Header() {
  return (
    <header className="bg-red-600 py-2 md:py-2 px-6 shadow-md fixed flex 
                         flex-col md:flex-row items-center justify-between 
                         w-full z-10 transition-all">
      <Link 
        to="/" 
        onClick={scrollToTop}>
        <div 
          id="title" 
          className="text-white text-xl md:text-4xl font-semibold 
                    cursor-pointer group">
          ElectroMarket
        </div>
      </Link>
      <div className="md:flex-grow md:ml-6 md:mt-0 mt-2 group">
        <Searchbar />
      </div>
      <div className="flex mt-2 md:mt-0 md:ml-0 md:mr-0 group flex-row">
        <Cart />
        <User />
      </div>
    </header>
  );
}

export default Header;
