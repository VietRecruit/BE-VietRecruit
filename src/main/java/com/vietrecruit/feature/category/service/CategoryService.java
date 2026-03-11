package com.vietrecruit.feature.category.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.category.dto.request.CategoryRequest;
import com.vietrecruit.feature.category.dto.response.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(UUID companyId, UUID userId, CategoryRequest request);

    CategoryResponse updateCategory(
            UUID companyId, UUID categoryId, UUID userId, CategoryRequest request);

    CategoryResponse getCategory(UUID companyId, UUID categoryId);

    Page<CategoryResponse> listCategories(UUID companyId, Pageable pageable);

    void deleteCategory(UUID companyId, UUID categoryId);
}
