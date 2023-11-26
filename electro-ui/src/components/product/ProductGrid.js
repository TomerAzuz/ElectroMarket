import React from 'react';
import Product from './Product';

function ProductGrid({ filteredProducts, setSelectedBrands }) {
  return (
    <div className="w-full px-4">
      <h2 className="mb-4">Found {filteredProducts.length} products</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 
                    xl:grid-cols-5 gap-4 max-w-sm md:max-w-none mx-0">
        {filteredProducts.map((product) => (
          <Product key={product.id} product={product} onClick={() => setSelectedBrands([])} />
        ))}
      </div>
    </div>
  );
}

export default ProductGrid;