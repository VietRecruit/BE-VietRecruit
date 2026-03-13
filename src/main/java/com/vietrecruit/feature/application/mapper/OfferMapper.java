package com.vietrecruit.feature.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vietrecruit.feature.application.dto.request.OfferCreateRequest;
import com.vietrecruit.feature.application.dto.response.OfferResponse;
import com.vietrecruit.feature.application.entity.Offer;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    OfferResponse toResponse(Offer offer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Offer toEntity(OfferCreateRequest request);
}
