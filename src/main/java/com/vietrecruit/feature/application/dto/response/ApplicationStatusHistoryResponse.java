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
public class ApplicationStatusHistoryResponse {
    private UUID id;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private String notes;
    private String changedByName;
    private Instant changedAt;
}
