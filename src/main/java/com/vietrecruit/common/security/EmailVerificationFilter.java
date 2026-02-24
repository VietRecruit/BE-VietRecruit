package com.vietrecruit.common.security;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietrecruit.common.exception.ApiErrorCode;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.feature.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationFilter extends OncePerRequestFilter {

    private static final Set<String> SKIP_PATTERNS =
            Set.of(
                    "/vietrecruit/auth/**",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/**",
                    "/health/**");

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldSkip(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String principal = authentication.getPrincipal().toString();
        try {
            java.util.UUID userId = java.util.UUID.fromString(principal);
            Boolean emailVerified = userRepository.findEmailVerifiedById(userId);

            if (Boolean.FALSE.equals(emailVerified)) {
                log.debug("Blocked unverified user: userId={}", userId);
                writeErrorResponse(response);
                return;
            }
        } catch (IllegalArgumentException e) {
            log.debug("Non-UUID principal, skipping verification check: {}", principal);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkip(String requestUri) {
        return SKIP_PATTERNS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    private void writeErrorResponse(HttpServletResponse response) throws IOException {
        ApiErrorCode errorCode = ApiErrorCode.AUTH_EMAIL_NOT_VERIFIED;
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> body = ApiResponse.failure(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
