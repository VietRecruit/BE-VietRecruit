package com.vietrecruit.feature.location.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationResponse {
    private UUID id;
    private String name;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
}
