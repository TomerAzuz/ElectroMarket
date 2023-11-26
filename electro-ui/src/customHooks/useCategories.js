import { useState, useEffect } from 'react';
import axiosInstance from '../axiosInterceptor';

function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axiosInstance.get('/category');
        setCategories(response.data);
      } catch (error) {
        console.error('Error fetching categories data', error);
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  return { categories, loading };
}

export default useCategories;
