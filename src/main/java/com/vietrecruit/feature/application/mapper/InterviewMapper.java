package com.vietrecruit.feature.application.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vietrecruit.feature.application.dto.response.InterviewResponse;
import com.vietrecruit.feature.application.dto.response.InterviewerResponse;
import com.vietrecruit.feature.application.entity.Interview;
import com.vietrecruit.feature.user.entity.User;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    @Mapping(target = "interviewers", source = "interviewers")
    InterviewResponse toResponse(Interview interview);

    List<InterviewerResponse> toInterviewerResponses(Set<User> users);

    InterviewerResponse toInterviewerResponse(User user);
}
