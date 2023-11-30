import { useEffect, useState } from 'react'; 
import { toast } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

import { usePage } from '../contexts/PageContext';
import axiosInstance from '../axiosInterceptor';

const useSearch = () => {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [debouncedQuery, setDebouncedQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { setPage } = usePage();

  const handleSearchClick = () => {
    if (query.trim().length > 1) {
      navigate(`/products/search/${query}`);
      setPage(0);
      clearAll();
    }
  };

  const handleResultClick = (result) => {
    setSearchResults([]);
    navigate(`/products/${result.name}`, {
      state: { product: result },
    });
  };

  const handleError = (error) => {
    console.error(error);
    toast.error('Error fetching products');
  };

  const clearAll = () => {
    setSearchResults([]);
    setQuery('');
    setDebouncedQuery('');
  };

  // Debounced query
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

  return {
    query,
    setQuery,
    searchResults,
    setSearchResults,
    handleResultClick,
    handleSearchClick,
    loading, 
    clearAll
  };
};

export default useSearch;