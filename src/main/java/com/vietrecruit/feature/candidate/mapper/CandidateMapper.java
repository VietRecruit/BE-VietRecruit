package com.vietrecruit.feature.candidate.mapper;

import java.util.Arrays;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vietrecruit.feature.candidate.dto.request.CandidateUpdateRequest;
import com.vietrecruit.feature.candidate.dto.response.CandidateProfileResponse;
import com.vietrecruit.feature.candidate.entity.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(target = "skills", source = "skills", qualifiedByName = "arrayToList")
    CandidateProfileResponse toResponse(Candidate candidate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "defaultCvUrl", ignore = true)
    @Mapping(target = "parsedCvText", ignore = true)
    @Mapping(target = "cvFileSizeBytes", ignore = true)
    @Mapping(target = "cvOriginalFilename", ignore = true)
    @Mapping(target = "cvContentType", ignore = true)
    @Mapping(target = "cvUploadedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "skills", source = "skills", qualifiedByName = "listToArray")
    void updateEntity(CandidateUpdateRequest request, @MappingTarget Candidate candidate);

    @Named("arrayToList")
    default List<String> arrayToList(String[] array) {
        if (array == null) return null;
        return Arrays.asList(array);
    }

    @Named("listToArray")
    default String[] listToArray(List<String> list) {
        if (list == null) return null;
        return list.toArray(new String[0]);
    }
}
