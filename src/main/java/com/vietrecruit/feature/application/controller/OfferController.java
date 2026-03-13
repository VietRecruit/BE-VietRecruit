package com.vietrecruit.feature.application.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vietrecruit.common.ApiConstants;
import com.vietrecruit.common.base.BaseController;
import com.vietrecruit.common.enums.ApiSuccessCode;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.common.security.SecurityUtils;
import com.vietrecruit.feature.application.dto.request.OfferCreateRequest;
import com.vietrecruit.feature.application.dto.request.OfferRespondRequest;
import com.vietrecruit.feature.application.dto.response.OfferResponse;
import com.vietrecruit.feature.application.service.OfferService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Offer Service", description = "Endpoints for managing job offers")
public class OfferController extends BaseController {

    private final OfferService offerService;

    @Operation(
            summary = "Create Offer",
            description = "HR creates a new offer for an application in OFFER status")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_COMPANY_ADMIN')")
    @PostMapping(ApiConstants.Application.ROOT + ApiConstants.Application.OFFERS)
    public ResponseEntity<ApiResponse<OfferResponse>> createOffer(
            @PathVariable UUID id, @Valid @RequestBody OfferCreateRequest request) {
        var companyId = resolveCompanyId();
        var userId = SecurityUtils.getCurrentUserId();
        var response = offerService.createOffer(id, companyId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ApiSuccessCode.OFFER_CREATE_SUCCESS, response));
    }

    @Operation(
            summary = "List Offers",
            description = "HR or candidate (own, SENT+ only) views offers for an application")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @GetMapping(ApiConstants.Application.ROOT + ApiConstants.Application.OFFERS)
    public ResponseEntity<ApiResponse<List<OfferResponse>>> listOffers(@PathVariable UUID id) {
        var userId = SecurityUtils.getCurrentUserId();
        var response = offerService.listOffers(id, userId);
        return ResponseEntity.ok(ApiResponse.success(ApiSuccessCode.OFFER_LIST_SUCCESS, response));
    }

    @Operation(summary = "Get Offer", description = "HR or candidate views offer detail")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @GetMapping(ApiConstants.Offer.ROOT + ApiConstants.Offer.GET)
    public ResponseEntity<ApiResponse<OfferResponse>> getOffer(@PathVariable UUID id) {
        var userId = SecurityUtils.getCurrentUserId();
        var response = offerService.getOffer(id, userId);
        return ResponseEntity.ok(ApiResponse.success(ApiSuccessCode.OFFER_FETCH_SUCCESS, response));
    }

    @Operation(summary = "Send Offer", description = "HR sends a DRAFT offer to the candidate")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_COMPANY_ADMIN')")
    @PutMapping(ApiConstants.Offer.ROOT + ApiConstants.Offer.SEND)
    public ResponseEntity<ApiResponse<OfferResponse>> sendOffer(@PathVariable UUID id) {
        var companyId = resolveCompanyId();
        var userId = SecurityUtils.getCurrentUserId();
        var response = offerService.sendOffer(id, companyId, userId);
        return ResponseEntity.ok(ApiResponse.success(ApiSuccessCode.OFFER_SEND_SUCCESS, response));
    }

    @Operation(
            summary = "Respond to Offer",
            description = "Candidate accepts or declines a SENT offer")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @PutMapping(ApiConstants.Offer.ROOT + ApiConstants.Offer.RESPOND)
    public ResponseEntity<ApiResponse<OfferResponse>> respondToOffer(
            @PathVariable UUID id, @Valid @RequestBody OfferRespondRequest request) {
        var userId = SecurityUtils.getCurrentUserId();
        var response = offerService.respondToOffer(id, userId, request);
        return ResponseEntity.ok(
                ApiResponse.success(ApiSuccessCode.OFFER_RESPOND_SUCCESS, response));
    }

    @Operation(summary = "Delete Offer", description = "HR deletes a DRAFT offer (soft delete)")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_COMPANY_ADMIN')")
    @DeleteMapping(ApiConstants.Offer.ROOT + ApiConstants.Offer.GET)
    public ResponseEntity<ApiResponse<Void>> deleteOffer(@PathVariable UUID id) {
        var companyId = resolveCompanyId();
        offerService.deleteOffer(id, companyId);
        return ResponseEntity.ok(ApiResponse.success(ApiSuccessCode.OFFER_DELETE_SUCCESS));
    }
}
