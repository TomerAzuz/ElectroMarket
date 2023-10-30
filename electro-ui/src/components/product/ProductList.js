import React, { useContext } from 'react'
import { PulseLoader } from 'react-spinners';

import Product from './Product';
import { ProductContext } from '../../contexts/ProductContext';
import { filterByCategory } from './productsFilter';

function ProductList({ categoryId }) {
    const { products, loading } = useContext(ProductContext);
    
    const filteredProducts = filterByCategory(products, categoryId);

      return (
        <div className="min-h-screen flex items-center justify-center">
          {loading ? (
            <div className="text-center">
              <PulseLoader size={15} color="blue" loading={loading} />
            </div>
          ) : (
            <section className="py-16">
              <div className="container mx-auto">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-4 max-w-sm md:max-w-none mx-0">
                  {filteredProducts.map((product) => (
                    <Product product={product} key={product.id} />
                  ))}
                </div>
              </div>
            </section>
          )}
        </div>
      );
    }
    
    export default ProductList;