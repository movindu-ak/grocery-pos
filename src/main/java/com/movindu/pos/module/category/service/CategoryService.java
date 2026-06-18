package com.movindu.pos.module.category.service;

import com.movindu.pos.module.category.dto.request.CategoryRequest;
import com.movindu.pos.module.category.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    List<CategoryResponse> getActiveCategories();

    List<CategoryResponse> searchCategories(String name);
}