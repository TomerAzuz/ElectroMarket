import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../axiosInterceptor';

const FeaturedProducts = () => {
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const navigate = useNavigate();

  const handleProductClick = (product) => {
    navigate(`/products/${product.id}`, {
      state: { product: product },
    });
  };

  useEffect(() => {
    const productIds = [8, 45, 89, 174];

    const fetchFeaturedProducts = async () => {
      try {
        const cachedProducts =
          JSON.parse(localStorage.getItem('featuredProducts')) || [];
        if (cachedProducts.length > 0) {
          // Use cached data if available
          setFeaturedProducts(cachedProducts);
        } else {
          const promises = productIds.map(async (productId) => {
            const response = await axiosInstance.get(`/products/${productId}`);
            return response.data;
          });
          const products = await Promise.all(promises);
          setFeaturedProducts(products);

          // Cache the fetched data
          localStorage.setItem('featuredProducts', JSON.stringify(products));
        }
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    if (featuredProducts.length === 0) {
      fetchFeaturedProducts();
    }
  }, [featuredProducts]);

  return (
    <div className="p-8 rounded-md shadow-md bg-gray-100">
      <h2 className="text-3xl lg:text-4xl xl:text-5xl font-extrabold mb-6 text-center 
                  text-gray-900 leading-tight tracking-wide">
        Featured Products
      </h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-4 gap-6">
        {featuredProducts.length > 0 ? (
          featuredProducts.map((product) => (
            <div
              key={product.id}
              className="relative overflow-hidden w-full h-full p-4 rounded-md flex flex-col justify-between 
                          items-center bg-white shadow-lg transition-transform transform hover:scale-105"
              onClick={() => handleProductClick(product)}
            >
              <div className="cursor-pointer max-h-[200px] lg:max-w-[300px] 
                              xl:max-w-[400px] overflow-hidden rounded-md">
                <img
                  src={product.imageUrl}
                  alt={product.name}
                  className="w-full h-full object-cover rounded-md transition-opacity 
                            duration-300 ease-in-out hover:opacity-75"
                  loading="lazy"
                />
              </div>
              <div className="cursor-pointer p-2  text-center">
                <h3 className="text-md font-semibold mb-2 hover:underline">
                  {product.name}
                </h3>
                {product.price && (
                  <p className="text-red-500 text-2xl font-semibold cursor-pointer">
                    ${product.price.toLocaleString('en-US', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </p>
                )}
              </div>
            </div>
          ))
        ) : (
          <p>Loading...</p>
        )}
      </div>
    </div>
  );
};

export default FeaturedProducts;
