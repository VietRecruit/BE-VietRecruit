package com.vietrecruit.feature.candidate.dto.response;

import java.time.Instant;

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
public class CvUploadResponse {

    private String cvUrl;
    private String cvOriginalFilename;
    private String cvContentType;
    private Long cvFileSizeBytes;
    private Instant cvUploadedAt;
}
