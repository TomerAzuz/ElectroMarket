export const filterByBrand = (products, brand) => {
    return products.filter(product => product.brand === brand);
};

export const filterByPriceRange = (products, minPrice, maxPrice) =>  {
    return products.filter(product => product.price >= minPrice && product.price <= maxPrice);
};

export const getBrands = (products) => {
    const brands = new Set();
    products.forEach((product) => {
        if (product.brand)  {
            brands.add(product.brand);
        }
    });
    return brands;
} 
