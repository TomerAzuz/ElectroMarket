import React, { useState } from 'react';
import { FiSearch } from 'react-icons/fi';

import SearchResults from './SearchResults';
import useSearch from '../../customHooks/useSearch';

function Searchbar() {
  const { query, 
          setQuery, 
          searchResults, 
          handleSearchClick, 
          handleResultClick } = useSearch();

  const [isFocused, setIsFocused] = useState(false);
  
  const handleBlur = () => {
    setTimeout(() => {
      if (!document.activeElement.closest('.search-results-container')) {
        setIsFocused(false);
      }
    }, 100);
  };

  return (
    <div className="relative text-center">
      <div className='mx-auto w-full lg:w-1/3 xl:w-1/4'>
        <div
          className={`relative rounded-full border 
            border-gray-300 focus:outline-none 
            focus:border-black w-full`}
        >
          <div className="absolute top-0 left-0 ml-2 mt-1.5">
            <FiSearch
              className="cursor-pointer mb-2
                       text-gray-500"
              size={32}
              onClick={handleSearchClick}
            />
          </div>
          <input
            type="text"
            id="searchInput"
            name="searchInput"
            className="py-2 pl-12 rounded-full w-full text-left"
            placeholder="Search..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onFocus={() => setIsFocused(true)}
            onBlur={handleBlur}
          />
        </div>
      </div>
      {searchResults.length > 0 && isFocused && (
      <div className="absolute left-1/2 transform -translate-x-1/2 
                      mt-20 lg:mt-4 bg-white border rounded shadow-md 
                      items-center search-results-container w-full 
                      md:w-1/2 xl:w-1/3 z-10">
        <SearchResults
          results={searchResults}
          handleResultClick={handleResultClick}
        />
      </div>
      )}
    </div>
);
}

export default Searchbar;
