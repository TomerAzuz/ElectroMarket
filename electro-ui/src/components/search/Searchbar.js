import React, { useEffect, useState } from 'react';
import { FiSearch } from 'react-icons/fi';
import { toast } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

import axiosInstance from '../../axiosInterceptor';
import { usePage } from '../../contexts/PageContext';
import SearchResults from './SearchResults';
import Loader from '../common/Loader';

function Searchbar() {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [debouncedQuery, setDebouncedQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [isFocused, setIsFocused] = useState(false);
  const { setPage } = usePage();
  const navigate = useNavigate();

  const clearAll = () => {
    setSearchResults([]);
    setQuery('');
    setDebouncedQuery('');
    setPage(0);
  };

  const handleResultClick = (result) => {
    setSearchResults([]);
    navigate(`/products/${result.name}`, {
      state: { product: result },
    });
    setIsFocused(false);
  };

  const handleSearchClick = () => {
    if (query.trim().length > 1) {
      navigate(`/products/search/${query}`);
      clearAll();
    }
  };

  const handleBlur = () => {
    setTimeout(() => {
      if (!document.activeElement.closest('.search-results-container')) {
        setIsFocused(false);
      }
    }, 100);
  };
  
  const handleError = (error) => {
    console.error(error);
    toast.error('Error fetching products');
  };

  useEffect(() => {
    const delay = setTimeout(() => {
      setDebouncedQuery(query.trim());
    }, 400);
    return () => clearTimeout(delay);
  }, [query]);

  useEffect(() => {
    const fetchResults = async () => {
      const cachedResults = localStorage.getItem(debouncedQuery);
      if (debouncedQuery.trim().length > 1) {
        setLoading(true);
        try {
          let results;
          if (cachedResults) {
            // Use cached results
            results = JSON.parse(cachedResults);
          } else {
            // Cache miss, fetch from api
            const response = await axiosInstance.get(`/products/search/${query}`);
            results = response.data.content;
            // Cache the results
            localStorage.setItem(debouncedQuery, JSON.stringify(results));
          }
          setSearchResults(results);
        } catch (error) {
          handleError(error);
        } finally {
          setLoading(false);
        }
      } else {
        setSearchResults([]);
      }
    };

    fetchResults();
  }, [query, debouncedQuery]);


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
      {loading ? (
        <Loader loading={loading}/>
        ) : (
        searchResults.length > 0 &&
        isFocused &&
        (
        <div className="absolute left-1/2 transform -translate-x-1/2 mt-20 
                       lg:mt-4 bg-white border rounded shadow-md items-center 
                       search-results-container w-full md:w-1/2 xl:w-1/3 z-10">
          <SearchResults
            results={searchResults}
            handleResultClick={handleResultClick}
          />
        </div>
        )
      )}
    </div>
  );
}

export default Searchbar;
