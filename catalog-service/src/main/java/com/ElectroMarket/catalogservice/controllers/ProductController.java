package com.ElectroMarket.catalogservice.controllers;

import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Iterable<Product> getProducts()  {
        return productService.viewProductList();
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable Long id)   {
        return productService.viewProductDetails(id);
    }

    @GetMapping("/category/{id}")
    public Page<Product> getProductsByCategory(@PathVariable("id") Long id,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestParam(name = "sort", defaultValue = "name,asc") String sort) {
        Sort sorting = extractSortParam(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);
        return productService.viewProductsByCategory(id, pageable);
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
    public Product updateProduct(@PathVariable Long id,
                                 @Valid @RequestBody Product product) {
        return productService.editProductDetails(id, product);
    }

    @GetMapping("/search/{query}")
    public Page<Product> search(@PathVariable(name = "query") String query,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size,
                                @RequestParam(name = "sort", defaultValue = "name,asc") String sort) {
        Sort sorting = extractSortParam(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);
        return productService.searchProducts(query, pageable);
    }

    Sort extractSortParam(String sort)  {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        String sortOrder = sortParts.length > 1 ? sortParts[1] : "asc";
        return Sort.by(
                sortOrder.equals("asc") ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField)
        );
    }
}
