package com.vietrecruit.feature.application.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vietrecruit.common.ApiConstants;
import com.vietrecruit.common.base.BaseController;
import com.vietrecruit.common.enums.ApiSuccessCode;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.common.security.SecurityUtils;
import com.vietrecruit.feature.application.dto.request.ScorecardCreateRequest;
import com.vietrecruit.feature.application.dto.response.ScorecardResponse;
import com.vietrecruit.feature.application.service.ScorecardService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Scorecard Service", description = "Endpoints for managing interview scorecards")
public class ScorecardController extends BaseController {

    private final ScorecardService scorecardService;

    @Operation(
            summary = "Submit Scorecard",
            description = "Assigned interviewer submits a scorecard for an interview")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PostMapping(ApiConstants.Interview.ROOT + ApiConstants.Interview.SCORECARDS)
    public ResponseEntity<ApiResponse<ScorecardResponse>> submitScorecard(
            @PathVariable UUID id, @Valid @RequestBody ScorecardCreateRequest request) {
        var userId = SecurityUtils.getCurrentUserId();
        var response = scorecardService.submitScorecard(id, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ApiSuccessCode.SCORECARD_CREATE_SUCCESS, response));
    }

    @Operation(
            summary = "List Scorecards",
            description = "HR views all scorecards for an interview")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_COMPANY_ADMIN')")
    @GetMapping(ApiConstants.Interview.ROOT + ApiConstants.Interview.SCORECARDS)
    public ResponseEntity<ApiResponse<List<ScorecardResponse>>> listScorecards(
            @PathVariable UUID id) {
        var companyId = resolveCompanyId();
        var response = scorecardService.listScorecards(id, companyId);
        return ResponseEntity.ok(
                ApiResponse.success(ApiSuccessCode.SCORECARD_LIST_SUCCESS, response));
    }
}
