import React, { useState, useContext } from 'react';
import SearchResults from "./SearchResults";
import { ProductContext } from '../../contexts/ProductContext';

function Searchbar() {
  const { products } = useContext(ProductContext);

  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = (query) => {
    if (query.trim() !== '') {
      const filteredResults = products.filter(item => item.name.toLowerCase().startsWith(query.toLowerCase()));
      setSearchResults(filteredResults);
    } else {
      setSearchResults([]);
    }
  };

  const onSearch = (event) => {
    const query = event.target.value;
    setQuery(query);
    handleSearch(query);
  };

  const handleResultClick = () => {
    setSearchResults([]);
  };

  return (
    <div className="text-center">
      <div className='mx-auto w-1/3 sm:w-1/3 md:w-1/4 lg:w-1/3 xl:w-1/3'>
        <input
          type="text"
          placeholder="Search..."
          className="py-4 px-8 rounded-full border border-gray-300 focus:outline-none focus:border-black w-full"
          value={query}
          onChange={onSearch}
        />
      </div>
      {searchResults.length > 0 && (
        <div className="absolute mt-4 w-full bg-white border rounded shadow-md">
          <SearchResults 
            results={searchResults.slice(0, 5)} 
            onResultClick={handleResultClick} />
        </div>
      )}
    </div>
  );
}

export default Searchbar;
