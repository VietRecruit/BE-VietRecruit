package com.vietrecruit.feature.user.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vietrecruit.common.ApiConstants;
import com.vietrecruit.common.base.BaseController;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.common.response.ApiSuccessCode;
import com.vietrecruit.common.response.PageResponse;
import com.vietrecruit.feature.user.dto.request.UserRequest;
import com.vietrecruit.feature.user.dto.response.UserResponse;
import com.vietrecruit.feature.user.service.UserService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.User.ROOT)
public class UserController extends BaseController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @PostMapping(ApiConstants.User.CREATE)
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.USER_CREATE_SUCCESS, userService.create(request)));
    }

    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @GetMapping(ApiConstants.User.GET)
    public ResponseEntity<ApiResponse<UserResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.success(ApiSuccessCode.USER_FETCH_SUCCESS, userService.get(id)));
    }

    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.USER_LIST_SUCCESS,
                        PageResponse.from(userService.list(pageable))));
    }

    @PutMapping(ApiConstants.User.UPDATE)
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.USER_UPDATE_SUCCESS, userService.update(id, request)));
    }

    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @DeleteMapping(ApiConstants.User.DELETE)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(ApiSuccessCode.USER_DELETE_SUCCESS));
    }
}
