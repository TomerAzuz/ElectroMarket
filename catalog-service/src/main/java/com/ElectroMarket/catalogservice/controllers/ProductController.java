package com.ElectroMarket.catalogservice.controllers;

import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
//
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<Product> get()  {
        return productService.viewProductList();
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable Long id)   {
        return productService.viewProductDetails(id);
    }

    @GetMapping("/categories/{id}")
    public Iterable<Product> getProductsByCategory(@PathVariable("category_name") String categoryName) {
        return productService.viewProductsByCategory(categoryName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product product)   {
        return productService.addProductToCatalog(product);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)   {
        productService.removeProductFromCatalog(id);
    }

    @PutMapping("{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.editProductDetails(id, product);
    }
}
