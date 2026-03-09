package com.vietrecruit.feature.category.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private UUID id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
