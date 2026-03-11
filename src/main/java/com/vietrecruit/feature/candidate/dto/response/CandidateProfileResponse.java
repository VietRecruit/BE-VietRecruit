package com.vietrecruit.feature.candidate.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Candidate profile response")
public class CandidateProfileResponse {

    private UUID id;
    private UUID userId;
    private String headline;
    private String summary;
    private String defaultCvUrl;
    private String cvOriginalFilename;
    private String cvContentType;
    private Long cvFileSizeBytes;
    private Instant cvUploadedAt;

    // Profile fields
    private String desiredPosition;
    private String desiredPositionLevel;
    private Short yearsOfExperience;
    private List<String> skills;
    private String primaryLanguage;
    private String workType;
    private Long desiredSalaryMin;
    private Long desiredSalaryMax;
    private LocalDate availableFrom;
    private String educationLevel;
    private String educationMajor;
    private Boolean isOpenToWork;

    private Instant createdAt;
    private Instant updatedAt;
}
