package com.rewcode.blog.mapper;

import com.rewcode.blog.entity.Category;
import com.rewcode.blog.payload.CategoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CategoryDto convertToCategoryDto(Category category){
        return modelMapper.map(category, CategoryDto.class);
    }

    public Category convertToCategory(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }

}
