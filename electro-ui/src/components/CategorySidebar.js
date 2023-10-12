import React, { useContext, useEffect, useState } from "react";
import { IoMdArrowBack } from 'react-icons/io';
import { Link  } from 'react-router-dom';

import { CategoryContext } from "../contexts/CategoryContext";

function CategorySidebar() {
    const { isOpenCategories, handleCloseCategories, categories, setSelectedCategory } = useContext(CategoryContext);

    const handleCategoryClick = (category) => {
      setSelectedCategory(category);
    };

    return (
        <div
          className={`${
            isOpenCategories ? 'left-0' : '-left-full'
          } w-full bg-white fixed top-0 h-full shadow-2xl md:w-[30vw] xl:max-w-[25vw] transition-all duration-300 z-20 px-4 lg:px-[30px]`}
        >
          <div className='flex items-center justify-between py-6 border-b'>
            <div onClick={handleCloseCategories} className='cursor-pointer w-8 h-8 flex justify-center items-center'>
              <IoMdArrowBack className='text-2xl' />
            </div>
            <div id="title" className='flex-grow text-center text-2xl'>ElectroMarket</div>
          </div>
          <div className="p-4">
            <ul>
              {categories.map(category => (
                <li key={category.id} className="mb-2">
                  <button
                    className="text-2xl cursor-pointer"
                    onClick={() => handleCategoryClick(category)}
                  >
                    <Link to={`/products/category/${category.id}`}>
                                {category.name}
                    </Link>
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </div>
      );
}

export default CategorySidebar;
