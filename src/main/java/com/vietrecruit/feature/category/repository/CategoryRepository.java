package com.vietrecruit.feature.category.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vietrecruit.feature.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Page<Category> findByCompanyId(UUID companyId, Pageable pageable);

    Optional<Category> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByCompanyIdAndName(UUID companyId, String name);

    boolean existsByCompanyIdAndNameAndIdNot(UUID companyId, String name, UUID excludeId);
}
