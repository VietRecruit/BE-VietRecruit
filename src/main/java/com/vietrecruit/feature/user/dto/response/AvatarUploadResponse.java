package com.vietrecruit.feature.user.dto.response;

import java.time.Instant;

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
@Schema(description = "Response after avatar upload")
public class AvatarUploadResponse {

    @Schema(description = "Public URL of the uploaded avatar")
    private String avatarUrl;

    @Schema(description = "Upload timestamp")
    private Instant uploadedAt;
}
