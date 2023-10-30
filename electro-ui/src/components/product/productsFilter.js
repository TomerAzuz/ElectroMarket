export function filterByCategory(products, categoryId)    {
    return products.filter(product => product.categoryId === categoryId);
};
