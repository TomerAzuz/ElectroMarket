import React, { createContext, useState, useEffect, useCallback  } from 'react';
import axios from 'axios';

export const ProductContext = createContext();

const ProductProvider = ({ children }) => {
  const [products, setProducts] = useState([]);
  const [selectedCategory] = useState(null);
  const [loading, setLoading] = useState(true);

  const handleError = (error) => {
    console.error('Error fetching data:', error);
    setLoading(false);
  };

  const fetchProducts = useCallback(async () => {
    try {
      const response = await axios.get('/products');
      setProducts(response.data);
      setLoading(false);
    } catch (error) {
      handleError(error);
    }
  }, []);

  const fetchByCategory =  useCallback(async (categoryId) => {
    try {
      const response = await axios.get(`/products/category/${categoryId}`);
      setProducts(response.data);
      setLoading(false);
    } catch (error) {
      handleError(error);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const filterProductsByCategory = useCallback((categoryId) => {
    if (categoryId) {
      setLoading(true);
      fetchByCategory(categoryId);
    }
  }, [fetchByCategory]);

  return (
    <ProductContext.Provider value={{ products, loading, selectedCategory, filterProductsByCategory }}>
      {children}
    </ProductContext.Provider>
  );
};

export default ProductProvider;
