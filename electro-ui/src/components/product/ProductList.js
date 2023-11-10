import React, { useState } from 'react';
import { PulseLoader } from 'react-spinners';
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';
import Product from './Product';
import useProducts from '../../customHooks/useProducts';

function ProductList({ endpoint, params, handleSort, setPage }) {
  const { products, setProducts, loading, totalPages } = useProducts({
    endpoint: endpoint,
    params: params,
  });
  const [selectedBrands, setSelectedBrands] = useState([]);
  const brands = [...new Set(products.map((product) => product.brand))];
  const minPrice = Math.min(...products.map((product) => product.price));
  const maxPrice = Math.max(...products.map((product) => product.price));
  const [priceRange, setPriceRange] = useState([minPrice, maxPrice]);

  const handleBrandChange = (e) => {
    const brand = e.target.value;
    setSelectedBrands((prevBrands) => {
      if (prevBrands.includes(brand)) {
        return prevBrands.filter((item) => item !== brand);
      } else {
        return [...prevBrands, brand];
      }
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
    const brandFilter = selectedBrands.length === 0 || selectedBrands.includes(product.brand);
    const priceFilter = product.price >= priceRange[0] && product.price <= priceRange[1];

    return brandFilter && priceFilter;
  })

  return (
    <div className="flex items-center justify-center">
      {loading ? (
        <div className="text-center">
          <PulseLoader size={15} color="black" loading={loading} />
        </div>
      ) : (
        <div className="container mx-auto flex">
          <div className="w-1/4">
            <div className="mb-4">
              <h3>Sort by:</h3>
              <select
                className="px-4 py-2 bg-white border rounded-lg w-full"
                value={params.sort}
                onChange={(e) => clearProducts(e)}
              >
                <option value="name,asc">Name (A-Z)</option>
                <option value="name,desc">Name (Z-A)</option>
                <option value="price,asc">Price (Low to High)</option>
                <option value="price,desc">Price (High to Low)</option>
              </select>
            </div>
            <div>
              <h3>Filter by Brand:</h3>
              {brands.map((brand) => (
                <label key={brand} className="block my-2">
                  <input
                    type="checkbox"
                    value={brand}
                    checked={selectedBrands.includes(brand)}
                    onChange={handleBrandChange}
                    className="mr-2"
                  />
                  <span className="text-gray-800">{brand}</span>
                </label>
              ))}
            </div>
            <div>
              <h3>Filter by Price Range</h3>
              <Slider
                range
                className="t-slider"
                min={minPrice}
                max={maxPrice}
                value={priceRange}
                onChange={handlePriceChange}
              />
              <div className="mt-2">
                <span>Min: {priceRange[0]}</span>
                <span className="ml-2">Max: {priceRange[1]}</span>
              </div>
            </div>
          </div>
          <div className="w-3/4 px-4">
            <h2 className="mb-4">{filteredProducts.length} products</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-4 max-w-sm md:max-w-none mx-0">
              {filteredProducts.map((product) => (
                <Product key={product.id} product={product} />
              ))}
            </div>
            <div className="mt-20 text-center">
              {params.page < totalPages - 1 && (
                <button
                  className="px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold"
                  onClick={() => setPage(params.page + 1)}
                  disabled={params.page === totalPages - 1}
                >
                  Load more products
                </button>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProductList;
