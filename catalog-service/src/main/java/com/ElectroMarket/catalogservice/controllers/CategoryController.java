package com.ElectroMarket.catalogservice.controllers;

import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("{id}")
    public Category getById(@PathVariable Long id)   {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category post(@Valid @RequestBody Category category)   {
        return categoryService.createCategory(category);
    }

    @PutMapping("{id}")
    public Category put(@PathVariable Long id, @Valid @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)   {
        categoryService.deleteCategoryById(id);
    }
}