package com.ElectroMarket.catalog.controllers;

import com.ElectroMarket.catalog.models.Product;
import com.ElectroMarket.catalog.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
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
    public Iterable<Product> getProductsByCategory(@PathVariable Long id) {
        return productService.viewProductsByCategory(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product post(@Valid @RequestBody Product product)   {
        return productService.addProductToCatalog(product);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)   {
        productService.removeProductFromCatalog(id);
    }

    @PutMapping("{id}")
    public Product put(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.editProductDetails(id, product);
    }
}
