package com.vietrecruit.feature.ai.cv.service;

import java.util.UUID;

import com.vietrecruit.feature.ai.cv.dto.CvImprovementResponse;

public interface CvImprovementService {

    /**
     * Analyzes the candidate's stored CV using the AI model and returns improvement suggestions.
     *
     * @param userId the candidate's user UUID
     * @return CV improvement response containing structured feedback and recommendations
     */
    CvImprovementResponse analyze(UUID userId);
}
