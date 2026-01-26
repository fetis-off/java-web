package com.university.cosmocats.controller;

import com.university.cosmocats.dto.category.CategoryResponseDto;
import com.university.cosmocats.dto.category.CreateCategoryRequestDto;
import com.university.cosmocats.dto.category.ProductCategoryData;
import com.university.cosmocats.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto requestDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequestDto updateRequestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, updateRequestDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ProductCategoryData> getAllProductsByCategoryId(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAllProductsByCategoryId(id));
    }
}
