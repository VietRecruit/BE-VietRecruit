package com.vietrecruit.feature.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for verifying email with OTP code")
public class VerifyOtpRequest {

    @Schema(
            description = "User's email address to verify",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    @NotBlank
    private String email;

    @Schema(
            description = "8-digit verification code sent to the email",
            example = "12345678",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "\\d{8}", message = "Code must be exactly 8 digits")
    private String code;
}
