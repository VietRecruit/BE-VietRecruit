package com.vietrecruit.feature.category.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vietrecruit.feature.category.dto.request.CategoryRequest;
import com.vietrecruit.feature.category.dto.response.CategoryResponse;
import com.vietrecruit.feature.category.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}
