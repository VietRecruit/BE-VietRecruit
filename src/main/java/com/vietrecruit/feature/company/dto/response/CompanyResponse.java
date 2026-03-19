package com.vietrecruit.feature.company.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyResponse {
    private UUID id;
    private String name;
    private String domain;
    private String website;
    private Instant createdAt;
    private Instant updatedAt;
}
