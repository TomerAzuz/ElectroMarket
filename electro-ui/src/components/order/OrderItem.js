import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axiosInstance from '../../axiosInterceptor';

const OrderItem = ({ productId, quantity }) => {
  const [product, setProduct] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProductById = async () => {
        try {
          const response = await axiosInstance.get(`/products/${productId}`);
          setProduct(response.data);
        } catch (error) {
          console.error("failed to fetch product with id: " + productId);
        }
      };
      fetchProductById();
  }, [productId]);

  const handleProductClick = () => {
    navigate(`/products/${product.name}`, {
      state: {product: product}
    });
  };

  return (
    <div className="text-sm text-primary border-b py-2">
      <li>
        {product && (
          <div
            onClick={handleProductClick}
            className="cursor-pointer text-lg text-primary hover:underline"
          >
            {product.name} - {quantity} x ${product.price.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </div>
        )}
      </li>
    </div>
  );
};

export default OrderItem;