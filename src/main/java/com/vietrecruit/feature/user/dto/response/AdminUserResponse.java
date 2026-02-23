package com.vietrecruit.feature.user.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

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
public class AdminUserResponse {

    private UUID id;
    private UUID companyId;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String location;
    private LocalDate dob;
    private String gender;
    private Boolean isActive;
    private Boolean isLocked;
    private Short failedAttempts;
    private Instant lockUntil;
    private Instant lastLoginAt;
    private Set<String> roles;
    private Instant createdAt;
    private Instant updatedAt;
}
