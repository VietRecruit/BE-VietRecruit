package com.vietrecruit.feature.candidate.dto.request;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateUpdateRequest {

    @Size(max = 255, message = "Headline must not exceed 255 characters")
    private String headline;

    private String summary;
}
