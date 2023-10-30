import React, { createContext, useState, useEffect, useCallback  } from 'react';
import axiosInstance from '../axiosInterceptor';

export const ProductContext = createContext();

const ProductProvider = ({ children }) => {
  const [products, setProducts] = useState([
    {
      "id": 1,
      "name": "ACER Aspire 3 15 A315-510P-35P7 - 15.6 inch - Intel Core i3-N305 - 4GB - 128 GB",
      "description": "",
      "price": 359.99,
      "categoryId": 1,
      "stock": 32,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_105173467?x=1800&y=1800&format=jpg&quality=80&sp=yes&strip=yes&ex=1800&ey=1800&align=center&resizesource&unsharp=1.5x1+0.7+0.02"
    }, {
      "id": 2,
      "name": "APPLE MacBook Air 13.3 (2020) - Silver M1 256 GB",
      "description": "",
      "price": 949.99,
      "categoryId": 2,
      "stock": 21,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_78976636?x=1800&y=1800&format=jpg&quality=80&sp=yes&strip=yes&ex=1800&ey=1800&align=center&resizesource&unsharp=1.5x1+0.7+0.02"
    }, {
      "id": 3,
      "name": "MICROSOFT Surface Laptop Studio - 14.4 inch - Intel Core i5 - 16 GB - 256 GB",
      "description": "",
      "price": 1549.99,
      "categoryId": 1,
      "stock": 97,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_91902577?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }, {
      "id": 4,
      "name": "ASUS Vivobook Go 14 E1404FA-NK002W - 14 inch - AMD Ryzen 5 - 8 GB - 512 GB",
      "description": "",
      "price": 555.99,
      "categoryId": 1,
      "stock": 51,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_106267854?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }
  ]);
  const [loading, setLoading] = useState(true);

  const handleError = (error) => {
    console.error('Error fetching product data:', error);
    setLoading(false);
  };

  const fetchProducts = useCallback(async () => {
    try {
      const response = await axiosInstance.get('/products');
      setProducts(response.data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  return (
    <ProductContext.Provider value={{ products, loading }}>
      {children}
    </ProductContext.Provider>
  );
};

export default ProductProvider;
