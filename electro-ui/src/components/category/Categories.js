import React, { useEffect, useCallback, useState } from 'react';
import { PulseLoader } from 'react-spinners';
import axiosInstance from '../../axiosInterceptor';
import Category from './Category';

function Categories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleError = (error) => {
    console.error('Error fetching categories data', error);
    setLoading(false);
  };

  const fetchCategories = useCallback(async() => {
    try {
      const response = await axiosInstance.get('/category');
      setCategories(response.data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  if (loading) {
    return (
      <div className="loading-container">
        <PulseLoader size={15} color="black" loading={loading} />
      </div>
    );
  }

  return (
    <section id="categories-section">
      <div className="container mx-auto">
        <div 
          className="grid grid-cols-1 
                      md:grid-cols-2 
                      lg:grid-cols-3 
                      xl:grid-cols-4 
                      gap-4 
                      max-w-screen-xl 
                      mx-auto"
        >
          {categories.map((category) => (
            <Category key={category.id} category={category} />
          ))}
        </div>
      </div>
    </section>
  );
}

export default Categories;
