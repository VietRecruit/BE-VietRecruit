package com.vietrecruit.feature.location.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vietrecruit.feature.location.dto.request.LocationRequest;
import com.vietrecruit.feature.location.dto.response.LocationResponse;
import com.vietrecruit.feature.location.entity.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationResponse toResponse(Location location);

    Location toEntity(LocationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(LocationRequest request, @MappingTarget Location location);
}
