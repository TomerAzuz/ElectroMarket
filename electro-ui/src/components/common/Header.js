import { Link } from "react-router-dom";

import Searchbar from "../search/Searchbar";
import User from "../user/User";
import Cart from "../cart/Cart";

function Header() {
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  return (
    <header className="bg-red-600 py-4 px-6 shadow-md fixed flex flex-col md:flex-row items-center justify-between w-full z-10 transition-all">
      <Link 
        to={'/'} 
        onClick={scrollToTop}>
        <div id="title" className="text-white text-4xl font-semibold cursor-pointer group">
          ElectroMarket
        </div>
      </Link>
      <div className="flex-grow ml-10 my-2 md:my-0 group">
        <Searchbar />
      </div>
      <div className="cursor-pointer flex text-black my-2 md:my-0 group">
        <Cart />
      </div>
      <div className="cursor-pointer flex items-center text-black ml-6 my-2 md:my-0 group">
        <User />
      </div>
    </header>
  );
}

export default Header;
