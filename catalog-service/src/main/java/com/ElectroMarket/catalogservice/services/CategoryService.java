package com.ElectroMarket.catalogservice.services;

import com.ElectroMarket.catalogservice.exceptions.ResourceAlreadyExistsException;
import com.ElectroMarket.catalogservice.exceptions.ResourceNotFoundException;
import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> getAllCategories()    {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id)    {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", id));
    }

    public Category createCategory(Category category)   {
        categoryRepository.findByName(category.name()).ifPresent(existingCategory -> {
            throw new ResourceAlreadyExistsException("category", category.id());
        });
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory)   {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    var categoryToUpdate = new Category(
                            existingCategory.id(),
                            updatedCategory.name(),
                            updatedCategory.imageUrl());
                    return categoryRepository.save(categoryToUpdate);
                })
                .orElseGet(() -> createCategory(updatedCategory));
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
