package com.vietrecruit.feature.department.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.department.dto.request.DepartmentRequest;
import com.vietrecruit.feature.department.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(UUID companyId, UUID userId, DepartmentRequest request);

    DepartmentResponse updateDepartment(
            UUID companyId, UUID departmentId, UUID userId, DepartmentRequest request);

    DepartmentResponse getDepartment(UUID companyId, UUID departmentId);

    Page<DepartmentResponse> listDepartments(UUID companyId, Pageable pageable);

    void deleteDepartment(UUID companyId, UUID departmentId);
}
