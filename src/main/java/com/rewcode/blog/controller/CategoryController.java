package com.rewcode.blog.controller;

import com.rewcode.blog.entity.Category;
import com.rewcode.blog.payload.CategoryDto;
import com.rewcode.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@Tag(
        name = "CRUD REST APIs for Category Resource"
)
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory (@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory (@PathVariable Long id){
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    public ResponseEntity<Set<CategoryDto>> getAllCategories (){
        Set<CategoryDto> categoryDtos = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryDtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategories (@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory (@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted");
    }
}
