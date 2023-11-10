import { useState, useEffect } from 'react';
import axiosInstance from '../axiosInterceptor';

function useProducts({ endpoint, params })    {
  const [products, setProducts] = useState([
    {
      "name": "ACER Aspire 3 15 - 15.6 inch - Intel Core i3-N305 - 4GB - 128 GB",
      "price": 359.99,
      "categoryId": 1,
      "stock": 32,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_105173467?x=1800&y=1800&format=jpg&quality=80&sp=yes&strip=yes&ex=1800&ey=1800&align=center&resizesource&unsharp=1.5x1+0.7+0.02",
      "brand": "ACER"
  },
  {
      "name": "APPLE MacBook Air 13.3 (2020) - Silver M1 256 GB",
      "price": 949.99,
      "categoryId": 1,
      "stock": 21,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_78976636?x=1800&y=1800&format=jpg&quality=80&sp=yes&strip=yes&ex=1800&ey=1800&align=center&resizesource&unsharp=1.5x1+0.7+0.02",
      "brand": "APPLE"
  },
  {
      "name": "MICROSOFT SURFACE LAPTOP 5 - 15.0 inch - Intel Core i7 - 8 GB - 256 GB",
      "price": 1529.0,
      "categoryId": 1,
      "stock": 23,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_103998082?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "MICROSOFT Surface Laptop Go 3 - 12.4 inch - Intel Core i5 - 8 GB - 256 GB",
      "price": 899.0,
      "categoryId": 1,
      "stock": 12,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_122626055?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "MICROSOFT SURFACE LAPTOP 5 - 13.5 inch - Intel Core i5 - 8 GB - 256 GB",
      "price": 1179.0,
      "categoryId": 1,
      "stock": 14,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_103573044?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "MICROSOFT Surface Laptop Go 3 - 12.4 inch - Intel Core i5 - 16 GB - 256 GB",
      "price": 1149.0,
      "categoryId": 1,
      "stock": 11,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_122625858?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "MICROSOFT SURFACE PRO 8 - 13.0 inch - Intel Core i5 - 8 GB - 512 GB",
      "price": 1479.0,
      "categoryId": 1,
      "stock": 31,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_90145733?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "MICROSOFT Surface Laptop Studio 2 - 14.4 inch - Intel Core i7 - 16 GB - 512 GB - GeForce RTX 4050",
      "price": 2719.0,
      "categoryId": 1,
      "stock": 8,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_122625794?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
      "name": "ASUS Chromebook CX1400CKA-EK0212 - 14 inch - Intel Celeron - 4 GB - 128 GB",
      "price": 369.0,
      "categoryId": 1,
      "stock": 12,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_106267880?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "ASUS"
  },
  {
      "name": "ASUS X515EA-EJ4051W - 15.6 inch - Intel Core i5 - 8 GB - 512 GB",
      "price": 499.0,
      "categoryId": 1,
      "stock": 9,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_108802070?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "ASUS"
  },
  {
      "name": "ASUS ROG Strix G16 G614JU-N3092W - 16 inch 165Hz - Intel Core i5 - 16GB - 512 GB - GeForce RTX 4050",
      "price": 1249.0,
      "categoryId": 1,
      "stock": 8,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_103804026?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "ASUS"
  },
  {
      "name": "ASUS ROG Strix G16 G614JV-N3110W - 16 inch 165Hz - Intel Core i7 - 16GB - 512 GB - GeForce RTX 4060",
      "price": 1549.0,
      "categoryId": 1,
      "stock": 6,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_103804132?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "ASUS"
  },
  {
      "name": "ASUS ROG FLOW X13 GV301RA-LJ052W - 13.3 inch - AMD Ryzen 9 - 32 GB - 1 TB",
      "price": 2299.0,
      "categoryId": 1,
      "stock": 7,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_94496599?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "ASUS"
  },
  {
      "name": "HP CHROMEBOOK 14B-CB0811ND - 14.0 inch - Intel Pentium Silver - 8 GB - 128 GB",
      "price": 549.0,
      "categoryId": 1,
      "stock": 4,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_85302893?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "HP"
  },
  {
      "name": "HP VICTUS 16R-0050ND - 16.1 inch - Intel Core i7 - 16 GB - 1 TB - GeForce RTX 4060",
      "price": 1499.0,
      "categoryId": 1,
      "stock": 10,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_106260101?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "HP"
  },
  {
      "name": "HP OMEN 16-N0150ND - 16.1 inch - AMD Ryzen 7 - 16 GB - 1 TB - GeForce RTX 3060",
      "price": 1337.0,
      "categoryId": 1,
      "stock": 5,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_97933631?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "HP"
  },
  {
      "name": "HP VICTUS 16-r0045nd - 16.1 inch - Intel Core i7 - 16 GB - 512 GB - GeForce RTX 4060",
      "price": 1349.0,
      "categoryId": 1,
      "stock": 6,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_114202566?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "HP"
  },
  {
      "name": "HP VICTUS 15-FA1888ND Gaming bundel + HyperX Cloud II Headset + HyperX Pulsefire Haste 2 Mouse",
      "price": 1279.0,
      "categoryId": 1,
      "stock": 9,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_110208551?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "HP"
  },
  {
      "name": "MEDION E15415 - 15.6 inch - Intel Core i5 - 8 GB - 512 GB",
      "price": 439.0,
      "categoryId": 1,
      "stock": 5,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_95861744?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MEDION"
  },
  {
      "name": "MEDION ERAZER DEPUTY - 15.6 inch - Intel Core i7 - 16 GB - 1 TB - GeForce RTX 3060",
      "price": 1599.0,
      "categoryId": 1,
      "stock": 18,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_95876187?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MEDION"
  },
  {
      "name": "MEDION S17405 - 17.3 inch - Intel Core i5 - 8 GB - 512 GB",
      "price": 749.0,
      "categoryId": 1,
      "stock": 14,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_88644963?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MEDION"
  },
  {
      "name": "MICROSOFT Surface Laptop Studio - 14.4 inch - Intel Core i5 - 16 GB - 256 GB",
      "price": 1549.99,
      "categoryId": 1,
      "stock": 97,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_91902577?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334",
      "brand": "MICROSOFT"
  }
  ]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    async function fetchProducts() {
      try {
        const response = await axiosInstance.get(endpoint, { params });
        const { content, totalPages } = response.data;
        setProducts(params.page > 0 ? (prevProducts) => [...prevProducts, ...content] : content);
        setTotalPages(totalPages);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    }
  
    fetchProducts();
  }, [endpoint, params, params.page, params.sort]);

  return { products, setProducts, loading, totalPages };
}

export default useProducts;