package com.vietrecruit.feature.user.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.user.dto.request.UserRequest;
import com.vietrecruit.feature.user.dto.response.AdminUserResponse;

public interface AdminUserService {
    AdminUserResponse create(UserRequest request);

    AdminUserResponse get(UUID id);

    Page<AdminUserResponse> list(Pageable pageable);

    AdminUserResponse update(UUID id, UserRequest request);

    void delete(UUID id);
}
