import React from 'react';
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';

function ProductFilter({
  params,
  brands,
  selectedBrands,
  handleBrandChange,
  minPrice,
  maxPrice,
  priceRange,
  handlePriceChange,
  clearProducts,
}) {
  return (
    <div className="w-1/8">
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
  );
}

export default ProductFilter;