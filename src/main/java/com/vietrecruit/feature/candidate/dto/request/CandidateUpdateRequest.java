package com.vietrecruit.feature.candidate.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
@Schema(description = "Payload for updating candidate profile")
public class CandidateUpdateRequest {

    @Schema(description = "Professional headline", example = "Senior Backend Engineer")
    @Size(max = 255, message = "Headline must not exceed 255 characters")
    private String headline;

    @Schema(description = "Professional summary")
    @Size(max = 5000, message = "Summary must not exceed 5000 characters")
    private String summary;

    @Schema(description = "Desired job position", example = "Backend Engineer")
    @Size(max = 100, message = "Desired position must not exceed 100 characters")
    private String desiredPosition;

    @Schema(description = "Target seniority level", example = "Senior")
    @Size(max = 50, message = "Position level must not exceed 50 characters")
    private String desiredPositionLevel;

    @Schema(description = "Total years of professional experience", example = "5")
    @Min(value = 0, message = "Years of experience must be at least 0")
    @Max(value = 50, message = "Years of experience must not exceed 50")
    private Short yearsOfExperience;

    @Schema(
            description = "List of technical skills",
            example = "[\"Java\", \"Spring Boot\", \"Kafka\"]")
    private List<String> skills;

    @Schema(description = "Primary programming language", example = "Java")
    @Size(max = 50, message = "Primary language must not exceed 50 characters")
    private String primaryLanguage;

    @Schema(description = "Preferred work arrangement", example = "REMOTE")
    @Pattern(
            regexp = "^(REMOTE|ONSITE|HYBRID)$",
            message = "Work type must be REMOTE, ONSITE, or HYBRID")
    private String workType;

    @Schema(description = "Minimum desired salary in VND", example = "15000000")
    @Min(value = 0, message = "Minimum salary must be at least 0")
    private Long desiredSalaryMin;

    @Schema(description = "Maximum desired salary in VND", example = "30000000")
    @Min(value = 0, message = "Maximum salary must be at least 0")
    private Long desiredSalaryMax;

    @Schema(description = "Earliest available start date")
    private LocalDate availableFrom;

    @Schema(description = "Highest education level", example = "Bachelor")
    @Size(max = 50, message = "Education level must not exceed 50 characters")
    private String educationLevel;

    @Schema(description = "Field of study", example = "Computer Science")
    @Size(max = 100, message = "Education major must not exceed 100 characters")
    private String educationMajor;

    @Schema(description = "Whether candidate is open to new opportunities")
    private Boolean isOpenToWork;
}
