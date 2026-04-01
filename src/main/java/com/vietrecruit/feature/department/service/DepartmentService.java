package com.vietrecruit.feature.department.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.department.dto.request.DepartmentRequest;
import com.vietrecruit.feature.department.dto.response.DepartmentResponse;

public interface DepartmentService {

    /**
     * Creates a new department within the given company.
     *
     * @param companyId the owning company's UUID
     * @param userId UUID of the user creating the department
     * @param request department name and optional description
     * @return the created department response
     */
    DepartmentResponse createDepartment(UUID companyId, UUID userId, DepartmentRequest request);

    /**
     * Updates the fields of an existing department owned by the company.
     *
     * @param companyId the owning company's UUID
     * @param departmentId the target department's UUID
     * @param userId UUID of the user performing the update
     * @param request updated department fields
     * @return the updated department response
     */
    DepartmentResponse updateDepartment(
            UUID companyId, UUID departmentId, UUID userId, DepartmentRequest request);

    /**
     * Returns a single department owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param departmentId the target department's UUID
     * @return the department response
     */
    DepartmentResponse getDepartment(UUID companyId, UUID departmentId);

    /**
     * Returns a paginated list of all departments for the given company.
     *
     * @param companyId the owning company's UUID
     * @param pageable pagination and sort parameters
     * @return page of department responses
     */
    Page<DepartmentResponse> listDepartments(UUID companyId, Pageable pageable);

    /**
     * Deletes a department owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param departmentId the target department's UUID
     */
    void deleteDepartment(UUID companyId, UUID departmentId);
}
