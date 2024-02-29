package com.rewcode.blog.service.impl;

import com.rewcode.blog.entity.Category;
import com.rewcode.blog.exception.ResourceNotFoundException;
import com.rewcode.blog.mapper.CategoryMapper;
import com.rewcode.blog.payload.CategoryDto;
import com.rewcode.blog.repository.CategoryRepository;
import com.rewcode.blog.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.convertToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.convertToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id));

        return categoryMapper.convertToCategoryDto(category);
    }

    @Override
    public Set<CategoryDto> getAllCategories() {
        Set<CategoryDto> categoryDtoSet = categoryRepository.findAll()
                .stream()
                .map(category -> categoryMapper.convertToCategoryDto(category))
                .collect(Collectors.toSet());

        return categoryDtoSet;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.convertToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
    }
}
