package com.vietrecruit.feature.application.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.enums.InterviewStatus;

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
public class InterviewResponse {
    private UUID id;
    private UUID applicationId;
    private String title;
    private Instant scheduledAt;
    private Integer durationMinutes;
    private String locationOrLink;
    private String interviewType;
    private InterviewStatus status;
    private List<InterviewerResponse> interviewers;
    private Instant createdAt;
}
