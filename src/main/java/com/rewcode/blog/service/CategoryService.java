package com.rewcode.blog.service;

import com.rewcode.blog.payload.CategoryDto;

import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long id);

    Set<CategoryDto> getAllCategories();

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

}
