import React from 'react';

const FeaturedProducts = () => {
  const featuredProducts = [
    {
      "name": "MICROSOFT Surface Laptop Studio 2 - 14.4 inch - Intel Core i7 - 16 GB - 512 GB - GeForce RTX 4050",
      "price": 2719.0,
      "categoryId": 1,
      "stock": 8,
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_122625794?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720",
      "brand": "MICROSOFT"
  },
  {
    "name": "LG OLED 55C24LA",
    "price": 1299.99,
    "categoryId": 3,
    "stock": 48,
    "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_93658951?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334",
    "brand": "LG"
  },
  {
    "name": "SAMSUNG Galaxy Z Fold5 5G - 512 GB",
    "price": 2009.99,
    "categoryId": 4,
    "stock": 52,
    "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_108450077?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334",
    "brand": "SAMSUNG"
  },
  {
    "name": "CANON Canon Powershot SX70 HS BLACK",
    "price": 598.99,
    "categoryId": 2,
    "stock": 26,
    "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/pixelboxx-mss-79951710?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334",
    "brand": "CANON"
},
  ];

  return (
    <div className="p-8 rounded-md shadow-md bg-gray-100">
      <h2 className="text-3xl lg:text-5xl font-bold mb-8 text-center 
                   text-gray-900 leading-tight">Featured Products</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-4 gap-6">
        {featuredProducts.map((product, index) => (
          <div key={index} className="p-4 rounded-md flex flex-col justify-between bg-white">
            <div className="mb-4">
              <img
                src={product.imageUrl}
                alt={product.name}
                className="w-full h-48 lg:h-full object-cover rounded-md" // Adjusted the height for larger screens
              />
            </div>
            <div className="flex flex-col items-center">
              <h3 className="text-md font-semibold mb-2">{product.name}</h3>
              <p className="text-red-500 text-2xl font-semibold">
                ${product.price.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FeaturedProducts;