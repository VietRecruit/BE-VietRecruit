package com.vietrecruit.feature.application.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.vietrecruit.feature.application.enums.ApplicationStatus;

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
public class ApplicationResponse {
    private UUID id;
    private UUID jobId;
    private String jobTitle;
    private UUID candidateId;
    private String candidateName;
    private String appliedCvUrl;
    private String coverLetter;
    private ApplicationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
