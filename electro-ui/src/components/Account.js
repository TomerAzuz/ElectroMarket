import React, { useState } from "react";
import { FaUser } from 'react-icons/fa';
import Cookies from 'js-cookie';

function Account({ user }) {

  function DropdownMenu() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const toggleDropdown = () => {
      setIsDropdownOpen(!isDropdownOpen);
    }

    const handleLogin = () => {
      window.location.href = '/oauth2/authorization/keycloak';
    }

    return (
      <div className="text-3xl relative">
        <div
          className={`rounded-full border w-12 h-12 flex justify-center items-center bg-white group-hover:bg-gray-300
              ${isDropdownOpen ? 'bg-gray-300' : ''}`}
          onClick={toggleDropdown}
        >
          <FaUser />
        </div>
        {isDropdownOpen && (
          <div className="absolute right-0 mt-12 p-4 bg-white border rounded-lg shadow-lg w-64">
            <ul>
              {user ? (
                <form action="/logout" 
                    method="post" 
                    className="cursor-pointer hover:bg-gray-200 p-2">
                    <input type="hidden" name="_csrf" value={Cookies.get('XSRF-TOKEN')} />
                    <button type="submit">Log Out</button>
                </form>
              ) : (
                  <li className="cursor-pointer hover:bg-gray-200 p-2" onClick={handleLogin}>
                    Log in
                  </li>
              )}
              <li className="cursor-pointer hover:bg-gray-200 p-2" onClick={() => {/* Handle 'My Account' click */}}>
                My Account
              </li>
              <li className="cursor-pointer hover:bg-gray-200 p-2" onClick={() => {/* Handle 'My Orders' click */}}>
                My Orders
              </li>
            </ul>
          </div>
        )}
      </div>
    );
  }

  return <DropdownMenu />;
}

export default Account;
