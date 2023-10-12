import React, { createContext, useState, useEffect } from 'react';
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

  const fetchProducts = async () => {
    try {
      const response = await axios.get('/products');
      setProducts(response.data);
      setLoading(false);
    } catch (error) {
      handleError(error);
    }
  };

  const fetchByCategory = async (categoryId) => {
    try {
      const response = await axios.get(`/products/category/${categoryId}`);
      setProducts(response.data);
      setLoading(false);
    } catch (error) {
      handleError(error);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const filterProductsByCategory = (categoryId) => {
    if (categoryId) {
      setLoading(true);
      fetchByCategory(categoryId);
    }
  };

  return (
    <ProductContext.Provider value={{ products, loading, selectedCategory, filterProductsByCategory }}>
      {children}
    </ProductContext.Provider>
  );
};

export default ProductProvider;
