package com.ElectroMarket.catalogservice.services;

import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.exceptions.ResourceAlreadyExistsException;
import com.ElectroMarket.catalogservice.exceptions.ResourceNotFoundException;
import com.ElectroMarket.catalogservice.repositories.CategoryRepository;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Product> viewProductList()    {
        return productRepository.findAll();
    }

    public Product viewProductDetails(Long id)    {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product", id));
    }

    public Product addProductToCatalog(Product product)   {
        productRepository.findById(product.id())
                .ifPresent(existingProduct -> {
                    throw new ResourceAlreadyExistsException("product", product.id());
                });
        categoryRepository.findById(product.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category", product.categoryId()));

        return productRepository.save(product);
    }

    public Page<Product> findProductsByCategory(Long id, Pageable pageable)  {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", null));
        return productRepository.findByCategoryId(category.id(), pageable);
    }

    public void removeProductFromCatalog(Long id)  {
        productRepository.deleteById(id);
    }

    public Product editProductDetails(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    var productToUpdate = new Product(
                            existingProduct.id(),
                            product.name(),
                            product.price(),
                            product.categoryId(),
                            product.stock(),
                            product.imageUrl(),
                            product.brand(),
                            existingProduct.createdDate(),
                            existingProduct.lastModifiedDate(),
                            existingProduct.createdBy(),
                            existingProduct.lastModifiedBy(),
                            existingProduct.version());
                    return productRepository.save(productToUpdate);
                })
                .orElseGet(() -> addProductToCatalog(product));
    }

    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }
}
