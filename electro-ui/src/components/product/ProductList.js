import React, { useState, useEffect } from 'react';

import useProducts from '../../customHooks/useProducts';
import ProductFilter from './ProductFilter';
import ProductGrid from './ProductGrid';
import Loader from '../common/Loader';
import LoadMoreButton from '../common/buttons/LoadMoreButton';

function ProductList({ endpoint, params, handleSort, setPage }) {
  const { products, setProducts, loading, totalPages } = useProducts({
    endpoint: endpoint,
    params: params,
  });

  const [selectedBrands, setSelectedBrands] = useState([]);
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(0);
  const brands = [...new Set(products.map((product) => product.brand))];
  const [priceRange, setPriceRange] = useState([minPrice, maxPrice]);

  useEffect(() => {
    if (products.length > 0) {
      const newMinPrice = Math.min(...products.map((product) => product.price));
      const newMaxPrice = Math.max(...products.map((product) => product.price));
      setMinPrice(newMinPrice);
      setMaxPrice(newMaxPrice);
      setPriceRange([minPrice, maxPrice]);
    }
  }, [products, selectedBrands, minPrice, maxPrice]);

  const handleBrandChange = (e) => {
    const brand = e.target.value;
    setSelectedBrands((prevBrands) => {
      return prevBrands.includes(brand)
        ? prevBrands.filter((item) => item !== brand)
        : [...prevBrands, brand];
    });
  };

  const clearProducts = (e) => {
    setProducts([]);
    handleSort(e);
  };

  const handlePriceChange = (value) => {
    setPriceRange(value);
  };

  const filteredProducts = products.filter((product) => {
    const brandFilter =
      selectedBrands.length === 0 || selectedBrands.includes(product.brand);
    const priceFilter =
      product.price >= priceRange[0] && product.price <= priceRange[1];

    return brandFilter && priceFilter;
  });

  return (
    <div>
      {loading ? (
        <Loader loading={loading} />
      ) : (
        <>
          <div className="container mx-auto flex">
            <ProductFilter
              params={params}
              brands={brands}
              selectedBrands={selectedBrands}
              handleBrandChange={handleBrandChange}
              minPrice={minPrice}
              maxPrice={maxPrice}
              priceRange={priceRange}
              handlePriceChange={handlePriceChange}
              clearProducts={clearProducts}
            />
            <ProductGrid
              filteredProducts={filteredProducts}
              setSelectedBrands={setSelectedBrands}
            />
          </div>
          <div className="mt-20 text-center">
            {params.page < totalPages - 1 && (
              <LoadMoreButton page={params.page}
                              setPage={setPage}
                              totalPages={totalPages}
              />
            )}
          </div>
        </>
      )}
    </div>
  );
}

export default ProductList;
