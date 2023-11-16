import React, { useEffect, useState } from 'react';
import { PulseLoader } from 'react-spinners';
import { FiSearch } from 'react-icons/fi';
import { toast } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

import axiosInstance from '../../axiosInterceptor';
import { scrollToTop } from '../common/utils';
import { usePage } from '../../contexts/PageContext';
import SearchResults from './SearchResults';

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
    scrollToTop();
  };

  const handleResultClick = (result) => {
    setSearchResults([]);
    navigate(`/products/${result.name}`, {
      state: { product: result },
    });
  };

  const handleSearchClick = () => {
    if (query.trim().length > 1) {
      navigate(`/products/search/${query}`);
      clearAll();
      scrollToTop();
    }
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
    <div className="text-center">
      <div className='relative mx-auto md:w-1/3 lg:w-1/3 xl:w-1/3'>
        <div
          className={`relative rounded-full border 
            border-gray-300 focus:outline-none 
            focus:border-black w-full`}
        >
          <div className="absolute top-0 left-0 ml-2 mt-1.5">
            <FiSearch
              className="cursor-pointer mb-2
                       text-gray-500 hover:bg-gray-200"
              size={32}
              onClick={handleSearchClick}
            />
          </div>
          <input
            type="text"
            className="py-2 pl-12 rounded-full w-full text-left"
            placeholder="Search..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
          />
        </div>
      </div>
      {loading ? (
        <PulseLoader
          className='items-center'
          size={15}
          color="black"
          loading={loading}
        />
      ) : (
        isFocused &&
        searchResults.length > 0 && (
          <div
            className="absolute mt-4 bg-white border rounded 
                       shadow-md w-full text-left"
          >
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
