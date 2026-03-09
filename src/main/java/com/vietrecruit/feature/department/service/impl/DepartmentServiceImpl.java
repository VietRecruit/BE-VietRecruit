package com.vietrecruit.feature.department.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vietrecruit.common.enums.ApiErrorCode;
import com.vietrecruit.common.exception.ApiException;
import com.vietrecruit.feature.department.dto.request.DepartmentRequest;
import com.vietrecruit.feature.department.dto.response.DepartmentResponse;
import com.vietrecruit.feature.department.entity.Department;
import com.vietrecruit.feature.department.mapper.DepartmentMapper;
import com.vietrecruit.feature.department.repository.DepartmentRepository;
import com.vietrecruit.feature.department.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(
            UUID companyId, UUID userId, DepartmentRequest request) {
        if (departmentRepository.existsByCompanyIdAndNameAndDeletedAtIsNull(
                companyId, request.getName())) {
            throw new ApiException(
                    ApiErrorCode.CONFLICT, "Department with this name already exists");
        }

        var department = departmentMapper.toEntity(request);
        department.setCompanyId(companyId);
        department.setCreatedBy(userId);
        department.setUpdatedBy(userId);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(
            UUID companyId, UUID departmentId, UUID userId, DepartmentRequest request) {
        var department = findByIdAndCompany(departmentId, companyId);

        if (departmentRepository.existsByCompanyIdAndNameAndIdNotAndDeletedAtIsNull(
                companyId, request.getName(), departmentId)) {
            throw new ApiException(
                    ApiErrorCode.CONFLICT, "Department with this name already exists");
        }

        departmentMapper.updateEntity(request, department);
        department.setUpdatedBy(userId);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(UUID companyId, UUID departmentId) {
        return departmentMapper.toResponse(findByIdAndCompany(departmentId, companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> listDepartments(UUID companyId, Pageable pageable) {
        return departmentRepository
                .findByCompanyIdAndDeletedAtIsNull(companyId, pageable)
                .map(departmentMapper::toResponse);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID companyId, UUID departmentId) {
        var department = findByIdAndCompany(departmentId, companyId);
        department.setDeletedAt(Instant.now());
        departmentRepository.save(department);
    }

    private Department findByIdAndCompany(UUID departmentId, UUID companyId) {
        return departmentRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(departmentId, companyId)
                .orElseThrow(
                        () -> new ApiException(ApiErrorCode.NOT_FOUND, "Department not found"));
    }
}
