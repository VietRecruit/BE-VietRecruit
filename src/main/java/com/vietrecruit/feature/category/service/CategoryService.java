package com.vietrecruit.feature.category.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.category.dto.request.CategoryRequest;
import com.vietrecruit.feature.category.dto.response.CategoryResponse;

public interface CategoryService {

    /**
     * Creates a new job category for the given company.
     *
     * @param companyId the owning company's UUID
     * @param userId UUID of the user creating the category
     * @param request category name and optional metadata
     * @return the created category response
     */
    CategoryResponse createCategory(UUID companyId, UUID userId, CategoryRequest request);

    /**
     * Updates the fields of an existing category owned by the company.
     *
     * @param companyId the owning company's UUID
     * @param categoryId the target category's UUID
     * @param userId UUID of the user performing the update
     * @param request updated category fields
     * @return the updated category response
     */
    CategoryResponse updateCategory(
            UUID companyId, UUID categoryId, UUID userId, CategoryRequest request);

    /**
     * Returns a single category owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param categoryId the target category's UUID
     * @return the category response
     */
    CategoryResponse getCategory(UUID companyId, UUID categoryId);

    /**
     * Returns a paginated list of all categories for the given company.
     *
     * @param companyId the owning company's UUID
     * @param pageable pagination and sort parameters
     * @return page of category responses
     */
    Page<CategoryResponse> listCategories(UUID companyId, Pageable pageable);

    /**
     * Deletes a category owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param categoryId the target category's UUID
     */
    void deleteCategory(UUID companyId, UUID categoryId);
}
