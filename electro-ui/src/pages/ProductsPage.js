import React, { useState } from 'react';
import { useParams , useLocation } from 'react-router-dom';

import ProductList from '../components/product/ProductList';
import { usePage } from '../contexts/PageContext';
import ErrorPage from './ErrorPage';

const ProductsPage = () => {    
  const location = useLocation();
  const category = location.state && location.state.category;
  const { query } = useParams();
  const { page, setPage } = usePage();
  const [sort, setSort] = useState('name,asc');  

  const endpoint = category ? `/products/category/${category.id}` : `/products/search/${query}`;
  const params = category ? {page: page, size: 10, sort: sort}  
                          : {page: page, sort: sort};

  const handleSort = (e) => {
    setSort(e.target.value);
    setPage(0);
  };

  if (category || query)  {
    return (
      <div className="flex-col items-center py-72 lg:py-32">
        <h1 className="text-4xl font-bold text-center text-primary">
          {category ? category.name : `Search Results for '${query}'`}
        </h1>
        <ProductList 
          endpoint={endpoint} 
          params={params}
          handleSort={handleSort}
          setPage={setPage}
        />
      </div>
    );
  } 
  return <ErrorPage />;
}; 

export default ProductsPage;
