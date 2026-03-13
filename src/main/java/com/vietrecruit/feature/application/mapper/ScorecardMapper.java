package com.vietrecruit.feature.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vietrecruit.feature.application.dto.response.ScorecardResponse;
import com.vietrecruit.feature.application.entity.Scorecard;

@Mapper(componentModel = "spring")
public interface ScorecardMapper {

    @Mapping(target = "interviewerName", ignore = true)
    ScorecardResponse toResponse(Scorecard scorecard);
}
