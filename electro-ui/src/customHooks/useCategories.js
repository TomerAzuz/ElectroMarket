import { useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInterceptor';

function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchCategories = useCallback(async () => {
    try {
      const response = await axiosInstance.get('/category');
      const newCategories = response.data;
      setCategories(newCategories);
    } catch (error) {
      console.error('Error fetching categories data', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  return { categories, loading };
}

export default useCategories;
