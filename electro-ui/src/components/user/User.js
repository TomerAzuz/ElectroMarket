import React, { useState, useContext, useEffect } from 'react';
import { FaUser } from 'react-icons/fa';
import Cookies from 'js-cookie';
import { Link } from 'react-router-dom';

import { AuthContext } from '../../contexts/AuthContext';

function User() {
  const { user, handleLogin, isEmployee } = useContext(AuthContext);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  const closeDropdown = () => {
    setIsDropdownOpen(false);
  };

  useEffect(() => {
    const handleBodyClick = (event) => {
      if (!event.target.closest('.dropdown-container')) {
        closeDropdown();
      }
    };
    document.body.addEventListener('mousedown', handleBodyClick);

    return () => {
      document.body.removeEventListener('mousedown', handleBodyClick);
    };
  }, []);

  return (
    <div className="text-3xl relative">
      <div
        className='rounded-full border w-12 h-12 
                    flex justify-center items-center
                  bg-white group hover:bg-gray-300'
        onClick={toggleDropdown}
      >
        <FaUser />
      </div>
      {isDropdownOpen && (
        <div className="absolute -right-0 mt-8 p-2 bg-white border rounded-lg shadow-lg w-40 dropdown-container">
          <ul>
            {user && isEmployee && (
              <Link to={'/admin'}>
                <div
                  className='text-lg hover:bg-gray-200 p-2'
                  onClick={closeDropdown}
                >
                  Admin
                </div>
              </Link>
            )}
            {user ? (
              <>
                <li className="text-lg cursor-pointer hover:bg-gray-200 p-2">
                  <Link to={'user/orders'} onClick={closeDropdown}>
                    My Orders
                  </Link>
                </li>
                <form
                  action="/logout"
                  method="post"
                  className="cursor-pointer text-lg hover:bg-gray-200 p-2"
                >
                  <input
                    type="hidden"
                    name="_csrf"
                    value={Cookies.get('XSRF-TOKEN')}
                  />
                  <button type="submit">Log out</button>
                </form>
              </>
            ) : (
              <li
                className="text-lg cursor-pointer hover:bg-gray-200 p-2"
                onClick={() => {
                  handleLogin();
                  closeDropdown();
                }}
              >
                Log In
              </li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
}

export default User;
