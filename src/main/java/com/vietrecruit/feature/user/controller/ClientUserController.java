package com.vietrecruit.feature.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vietrecruit.common.ApiConstants;
import com.vietrecruit.common.base.BaseController;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.common.response.ApiSuccessCode;
import com.vietrecruit.feature.user.dto.request.UpdateProfileRequest;
import com.vietrecruit.feature.user.dto.response.UserProfileResponse;
import com.vietrecruit.feature.user.service.ClientUserService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.ClientUser.ROOT)
public class ClientUserController extends BaseController {

    private final ClientUserService clientUserService;

    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @GetMapping(ApiConstants.ClientUser.ME)
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.USER_FETCH_SUCCESS, clientUserService.getProfile()));
    }

    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @PutMapping(ApiConstants.ClientUser.ME)
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.USER_UPDATE_SUCCESS,
                        clientUserService.updateProfile(request)));
    }
}
