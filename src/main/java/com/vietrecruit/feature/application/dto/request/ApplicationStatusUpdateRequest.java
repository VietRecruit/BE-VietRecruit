package com.vietrecruit.feature.application.dto.request;

import jakarta.validation.constraints.NotNull;

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
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Target status is required")
    private ApplicationStatus status;

    private String notes;
}
