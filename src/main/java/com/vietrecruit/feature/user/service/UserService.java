package com.vietrecruit.feature.user.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.user.dto.request.UserRequest;
import com.vietrecruit.feature.user.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserRequest request);

    UserResponse get(UUID id);

    Page<UserResponse> list(Pageable pageable);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);
}
