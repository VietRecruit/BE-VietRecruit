package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.request.InterviewCreateRequest;
import com.vietrecruit.feature.application.dto.request.InterviewStatusUpdateRequest;
import com.vietrecruit.feature.application.dto.response.InterviewResponse;

public interface InterviewService {

    /**
     * Schedules a new interview for an application, enforcing company ownership and status rules.
     *
     * @param applicationId the application being interviewed
     * @param companyId the company scheduling the interview
     * @param createdBy UUID of the user creating the interview
     * @param request interview details including time, location, and interviewer list
     * @return the created interview response
     */
    InterviewResponse scheduleInterview(
            UUID applicationId, UUID companyId, UUID createdBy, InterviewCreateRequest request);

    /**
     * Returns all interviews associated with an application, scoped to the owning company.
     *
     * @param applicationId the target application's UUID
     * @param companyId the owning company's UUID
     * @return list of interview responses
     */
    List<InterviewResponse> listInterviews(UUID applicationId, UUID companyId);

    /**
     * Returns a single interview visible to the requesting user (company member or assigned
     * interviewer).
     *
     * @param interviewId the target interview's UUID
     * @param userId the requesting user's UUID
     * @return the interview response
     */
    InterviewResponse getInterview(UUID interviewId, UUID userId);

    /**
     * Transitions an interview's status (e.g. SCHEDULED to COMPLETED or CANCELED).
     *
     * @param interviewId the target interview's UUID
     * @param companyId the owning company's UUID
     * @param userId the user performing the update
     * @param request the new status and optional notes
     * @return the updated interview response
     */
    InterviewResponse updateInterviewStatus(
            UUID interviewId, UUID companyId, UUID userId, InterviewStatusUpdateRequest request);

    /**
     * Returns all interviews to which the given user has been assigned as an interviewer.
     *
     * @param userId the interviewer's UUID
     * @return list of interview responses
     */
    List<InterviewResponse> getInterviewsByInterviewer(UUID userId);
}
