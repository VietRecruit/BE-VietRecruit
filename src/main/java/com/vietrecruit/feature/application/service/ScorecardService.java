package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.request.ScorecardCreateRequest;
import com.vietrecruit.feature.application.dto.response.ScorecardResponse;

public interface ScorecardService {

    /**
     * Submits an interviewer's scorecard for a completed interview, enforcing one-per-interviewer
     * uniqueness.
     *
     * @param interviewId the evaluated interview's UUID
     * @param userId the submitting interviewer's UUID
     * @param request scorecard fields including rating and comments
     * @return the persisted scorecard response
     */
    ScorecardResponse submitScorecard(
            UUID interviewId, UUID userId, ScorecardCreateRequest request);

    /**
     * Returns all scorecards submitted for an interview, scoped to the owning company.
     *
     * @param interviewId the target interview's UUID
     * @param companyId the owning company's UUID
     * @return list of scorecard responses
     */
    List<ScorecardResponse> listScorecards(UUID interviewId, UUID companyId);
}
