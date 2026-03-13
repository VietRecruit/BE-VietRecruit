package com.vietrecruit.feature.application.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.vietrecruit.feature.application.enums.ScorecardResult;

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
public class ScorecardResponse {
    private UUID id;
    private UUID interviewId;
    private UUID interviewerId;
    private String interviewerName;
    private BigDecimal skillScore;
    private BigDecimal attitudeScore;
    private BigDecimal englishScore;
    private BigDecimal averageScore;
    private ScorecardResult result;
    private String comments;
    private Instant createdAt;
}
