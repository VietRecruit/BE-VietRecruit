package com.vietrecruit.feature.user.dto.request;

import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Request to set an external URL for avatar or banner")
public class ExternalUrlRequest {

    @Schema(
            description = "External image URL (must be HTTPS)",
            example = "https://example.com/photo.jpg")
    @NotBlank(message = "URL must not be blank")
    @Size(max = 500, message = "URL must not exceed 500 characters")
    @Pattern(regexp = "^https://.*", message = "URL must start with https://")
    private String url;
}
