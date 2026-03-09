package com.vietrecruit.common.base;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vietrecruit.common.enums.ApiErrorCode;
import com.vietrecruit.common.exception.ApiException;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.common.security.SecurityUtils;
import com.vietrecruit.feature.user.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

public abstract class BaseController {

    @Autowired private UserRepository userRepository;

    /**
     * Resolves the company ID from the currently authenticated user. Throws FORBIDDEN if the user
     * is not associated with a company.
     */
    protected UUID resolveCompanyId() {
        var userId = SecurityUtils.getCurrentUserId();
        return userRepository
                .findById(userId)
                .map(
                        user -> {
                            if (user.getCompanyId() == null) {
                                throw new ApiException(
                                        ApiErrorCode.FORBIDDEN,
                                        "User is not associated with any company");
                            }
                            return user.getCompanyId();
                        })
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND, "User not found"));
    }

    /**
     * Fallback method for rate limiter. Only handles RequestNotPermitted exceptions. Business
     * exceptions will propagate to GlobalExceptionHandler.
     */
    public ResponseEntity<ApiResponse<Void>> rateLimit(RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ApiResponse.failure(ApiErrorCode.TOO_MANY_REQUESTS));
    }

    /**
     * Fallback method for circuit breaker. Only handles CallNotPermittedException. Business
     * exceptions will propagate to GlobalExceptionHandler.
     */
    public ResponseEntity<ApiResponse<Void>> circuitBreaker(CallNotPermittedException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.failure(ApiErrorCode.SERVICE_UNAVAILABLE));
    }
}
