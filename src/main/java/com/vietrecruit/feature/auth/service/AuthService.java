package com.vietrecruit.feature.auth.service;

import java.util.Map;
import java.util.UUID;

import com.vietrecruit.feature.auth.dto.request.ChangePasswordRequest;
import com.vietrecruit.feature.auth.dto.request.ForgotPasswordRequest;
import com.vietrecruit.feature.auth.dto.request.LoginRequest;
import com.vietrecruit.feature.auth.dto.request.RegisterByInviteRequest;
import com.vietrecruit.feature.auth.dto.request.RegisterRequest;
import com.vietrecruit.feature.auth.dto.request.ResendOtpRequest;
import com.vietrecruit.feature.auth.dto.request.ResetPasswordRequest;
import com.vietrecruit.feature.auth.dto.request.TokenRefreshRequest;
import com.vietrecruit.feature.auth.dto.request.VerifyOtpRequest;
import com.vietrecruit.feature.auth.dto.response.LoginResponse;
import com.vietrecruit.feature.auth.dto.response.TokenRefreshResponse;

public interface AuthService {

    /**
     * Authenticates a user with email and password and returns JWT tokens.
     *
     * @param request login credentials
     * @return access and refresh token pair
     */
    LoginResponse login(LoginRequest request);

    /**
     * Registers a new candidate or employer account and dispatches an OTP verification email.
     *
     * @param request registration payload including account type
     * @return map containing the resolved {@code accountType}
     */
    Map<String, Object> register(RegisterRequest request);

    /**
     * Registers a new user via an invitation token, assigning the invitation's role and company.
     *
     * @param request invitation token and new user credentials
     */
    void registerByInvite(RegisterByInviteRequest request);

    /**
     * Exchanges a valid refresh token for a new access token.
     *
     * @param request refresh token payload
     * @return new access token response
     */
    TokenRefreshResponse refresh(TokenRefreshRequest request);

    /**
     * Invalidates the provided access token by adding its JTI to the blacklist.
     *
     * @param accessToken the raw Bearer token string
     */
    void logout(String accessToken);

    /**
     * Initiates the password reset flow by sending a reset link to the registered email address.
     *
     * @param request contains the user's email address
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Changes the authenticated user's password and invalidates all existing sessions.
     *
     * @param userId the authenticated user's UUID
     * @param request current and new password payload
     */
    void changePassword(UUID userId, ChangePasswordRequest request);

    /**
     * Resets the user's password using the token from the forgot-password email.
     *
     * @param request reset token and new password payload
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * Verifies the email OTP code and marks the user's email as verified.
     *
     * @param request email address and OTP code
     */
    void verifyOtp(VerifyOtpRequest request);

    /**
     * Resends the email verification OTP, subject to cooldown and lockout constraints.
     *
     * @param request email address to resend OTP to
     */
    void resendOtp(ResendOtpRequest request);

    /**
     * Handles OAuth2 social login: creates or updates the user account and returns JWT tokens.
     *
     * @param provider OAuth2 provider name (e.g. GOOGLE, GITHUB)
     * @param email verified email from the provider
     * @param providerUserId provider-specific user identifier
     * @param providerName display name from the provider
     * @param providerAvatarUrl avatar URL from the provider, may be null
     * @param providerEmailVerified whether the provider confirmed the email is verified
     * @return access and refresh token pair
     */
    LoginResponse processOAuth2Login(
            String provider,
            String email,
            String providerUserId,
            String providerName,
            String providerAvatarUrl,
            Boolean providerEmailVerified);
}
