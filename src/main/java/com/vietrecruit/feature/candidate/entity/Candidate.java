package com.vietrecruit.feature.candidate.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(length = 255)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "default_cv_url", length = 255)
    private String defaultCvUrl;

    @Column(name = "parsed_cv_text", columnDefinition = "TEXT")
    private String parsedCvText;

    @Column(name = "cv_file_size_bytes")
    private Long cvFileSizeBytes;

    @Column(name = "cv_original_filename", length = 255)
    private String cvOriginalFilename;

    @Column(name = "cv_content_type", length = 100)
    private String cvContentType;

    @Column(name = "cv_uploaded_at")
    private Instant cvUploadedAt;

    // --- Profile fields for LLM auto-fill and job matching ---

    @Column(name = "desired_position", length = 100)
    private String desiredPosition;

    @Column(name = "desired_position_level", length = 50)
    private String desiredPositionLevel;

    @Column(name = "years_of_experience")
    private Short yearsOfExperience;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills", columnDefinition = "TEXT[]")
    private String[] skills;

    @Column(name = "primary_language", length = 50)
    private String primaryLanguage;

    @Column(name = "work_type", length = 20)
    private String workType;

    @Column(name = "desired_salary_min")
    private Long desiredSalaryMin;

    @Column(name = "desired_salary_max")
    private Long desiredSalaryMax;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "education_level", length = 50)
    private String educationLevel;

    @Column(name = "education_major", length = 100)
    private String educationMajor;

    @Builder.Default
    @Column(name = "is_open_to_work")
    private Boolean isOpenToWork = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
