import { useState, useEffect } from 'react';
import axiosInstance from '../axiosInterceptor';

function useProducts({ endpoint, params })    {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    async function fetchProducts() {
      try {
        const response = await axiosInstance.get(endpoint, { params });
        const { content, totalPages } = response.data;
        setProducts(params.page > 0 ? 
          (prevProducts) => [...prevProducts, ...content] : content);
        setTotalPages(totalPages);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    }
    fetchProducts();
  }, [endpoint, params, params.page, params.sort]);
  
  return { products, setProducts, loading, totalPages };
}

export default useProducts;