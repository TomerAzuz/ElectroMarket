import React, { useEffect, useContext } from 'react'
import { ProductContext } from '../contexts/ProductContext';
import { CategoryContext } from '../contexts/CategoryContext';
import ProductList from '../components/ProductList';

const Category = () => {
    const { selectedCategory } = useContext(CategoryContext);
    const { products, filterProductsByCategory } = useContext(ProductContext);

    useEffect(() => {
        if (selectedCategory) {
          filterProductsByCategory(selectedCategory.id);
        }
      }, [selectedCategory, filterProductsByCategory]);

    return (
        <div>
            <h2>Products by Category: {selectedCategory ? selectedCategory.name : 'All'}</h2>
            <ProductList products={products} />
        </div>
    );
}

export default Category;