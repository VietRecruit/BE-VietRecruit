package com.vietrecruit.feature.user.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

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
public class UpdateProfileRequest {

    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String phone;

    private String avatarUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String location;
    private LocalDate dob;
    private String gender;
}
