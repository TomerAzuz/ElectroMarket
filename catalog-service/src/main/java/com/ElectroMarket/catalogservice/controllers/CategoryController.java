package com.ElectroMarket.catalogservice.controllers;

import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.services.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Iterable<Category> getAllCategories() {
        log.info("Fetching the list of categories in the catalog");
        return categoryService.getAllCategories();
    }

    @GetMapping("{id}")
    public Category getById(@PathVariable Long id)   {
        log.info("Fetching the category with id {} from the catalog", id);
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category post(@Valid @RequestBody Category category)   {
        log.info("Adding a new category to the catalog with id {}", category.id());
        return categoryService.createCategory(category);
    }

    @PutMapping("{id}")
    public Category put(@PathVariable Long id, @Valid @RequestBody Category category) {
        log.info("Updating category with ISBN {}", id);
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)   {
        log.info("Deleting category with id {}", id);
        categoryService.deleteCategoryById(id);
    }
}