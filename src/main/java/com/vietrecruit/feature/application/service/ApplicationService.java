package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.vietrecruit.common.response.PageResponse;
import com.vietrecruit.feature.application.dto.request.ApplicationCreateRequest;
import com.vietrecruit.feature.application.dto.request.ApplicationStatusUpdateRequest;
import com.vietrecruit.feature.application.dto.response.ApplicationResponse;
import com.vietrecruit.feature.application.dto.response.ApplicationStatusHistoryResponse;
import com.vietrecruit.feature.application.dto.response.ApplicationSummaryResponse;
import com.vietrecruit.feature.application.enums.ApplicationStatus;

public interface ApplicationService {

    /**
     * Submits a new application for the given user and job, returning the created application.
     *
     * @param userId the applying candidate's UUID
     * @param request application payload including job ID and optional CV reference
     * @return the created application response
     */
    ApplicationResponse apply(UUID userId, ApplicationCreateRequest request);

    /**
     * Returns a single application visible to the requesting user (candidate or company member).
     *
     * @param applicationId the target application's UUID
     * @param userId the requesting user's UUID
     * @return the application response
     */
    ApplicationResponse getApplication(UUID applicationId, UUID userId);

    /**
     * Returns a paginated list of applications scoped to a company, optionally filtered by job and
     * status.
     *
     * @param companyId the owning company's UUID
     * @param jobId optional job filter; pass null to include all jobs
     * @param status optional status filter; pass null to include all statuses
     * @param pageable pagination and sort parameters
     * @return page of application summaries
     */
    PageResponse<ApplicationSummaryResponse> listApplications(
            UUID companyId, UUID jobId, ApplicationStatus status, Pageable pageable);

    /**
     * Returns a paginated list of all applications submitted by the given candidate.
     *
     * @param userId the candidate's UUID
     * @param pageable pagination and sort parameters
     * @return page of application summaries
     */
    PageResponse<ApplicationSummaryResponse> listMyApplications(UUID userId, Pageable pageable);

    /**
     * Advances or rejects an application's status, appending a history record.
     *
     * @param applicationId the target application's UUID
     * @param companyId the company performing the update
     * @param userId the user performing the update
     * @param request new status and optional notes
     * @return the updated application response
     */
    ApplicationResponse updateStatus(
            UUID applicationId,
            UUID companyId,
            UUID userId,
            ApplicationStatusUpdateRequest request);

    /**
     * Returns the full status-change history for an application owned by the given company.
     *
     * @param applicationId the target application's UUID
     * @param companyId the owning company's UUID
     * @return ordered list of status history entries
     */
    List<ApplicationStatusHistoryResponse> getStatusHistory(UUID applicationId, UUID companyId);

    /**
     * Persists a status-change history record; called internally whenever status transitions occur.
     *
     * @param applicationId the application being tracked
     * @param oldStatus the previous status
     * @param newStatus the new status
     * @param changedBy UUID of the user who triggered the change
     * @param notes optional free-text notes attached to the transition
     */
    void insertHistory(
            UUID applicationId,
            ApplicationStatus oldStatus,
            ApplicationStatus newStatus,
            UUID changedBy,
            String notes);
}
