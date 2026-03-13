package com.vietrecruit.feature.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vietrecruit.feature.application.dto.response.ApplicationResponse;
import com.vietrecruit.feature.application.dto.response.ApplicationSummaryResponse;
import com.vietrecruit.feature.application.entity.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "candidateName", ignore = true)
    ApplicationResponse toResponse(Application application);

    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "candidateName", ignore = true)
    ApplicationSummaryResponse toSummaryResponse(Application application);
}
