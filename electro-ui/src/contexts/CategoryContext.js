import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const CategoryContext = createContext();

const CategoryProvider = ({ children }) => {
    const [isOpenCategories, setIsOpenCategories] = useState(false);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    useEffect(() => {
        const fetchCategories = async () => {
          try {
            const response = await axios.get('/category'); 
            console.log(response.data);
            setCategories(response.data);
          } catch (error) {
            console.error('Error fetching data:', error);
          }
        };
        fetchCategories();
      }, []); 

    const handleCloseCategories = () => {
        setIsOpenCategories(false);
    }

    return (
        <CategoryContext.Provider value={{ isOpenCategories, setIsOpenCategories, handleCloseCategories, 
        categories, selectedCategory, setSelectedCategory}}>
            {children}
        </CategoryContext.Provider>
    );
};

export default CategoryProvider;