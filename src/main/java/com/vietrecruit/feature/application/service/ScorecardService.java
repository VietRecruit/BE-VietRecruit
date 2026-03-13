package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.request.ScorecardCreateRequest;
import com.vietrecruit.feature.application.dto.response.ScorecardResponse;

public interface ScorecardService {

    ScorecardResponse submitScorecard(
            UUID interviewId, UUID userId, ScorecardCreateRequest request);

    List<ScorecardResponse> listScorecards(UUID interviewId, UUID companyId);
}
