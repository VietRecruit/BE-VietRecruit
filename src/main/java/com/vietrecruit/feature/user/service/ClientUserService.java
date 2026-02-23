package com.vietrecruit.feature.user.service;

import com.vietrecruit.feature.user.dto.request.UpdateProfileRequest;
import com.vietrecruit.feature.user.dto.response.UserProfileResponse;

public interface ClientUserService {
    UserProfileResponse getProfile();

    UserProfileResponse updateProfile(UpdateProfileRequest request);
}
