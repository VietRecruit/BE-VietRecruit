package com.vietrecruit.feature.ai.jd.service;

import java.util.UUID;

import com.vietrecruit.feature.ai.jd.dto.ApplyDescriptionRequest;
import com.vietrecruit.feature.ai.jd.dto.JdGenerateRequest;
import com.vietrecruit.feature.ai.jd.dto.JdGenerateResponse;

public interface JdGeneratorService {

    /**
     * Generates a complete, bias-free job description from minimal inputs without persisting it.
     *
     * @param request generation parameters including title, requirements, and tone preferences
     * @param companyId the requesting company's UUID, used for context enrichment
     * @return the generated job description response
     */
    JdGenerateResponse generate(JdGenerateRequest request, UUID companyId);

    /**
     * Applies a previously generated description text to an existing job posting.
     *
     * @param jobId the target job's UUID
     * @param companyId the owning company's UUID
     * @param request the description content to apply
     */
    void applyDescription(UUID jobId, UUID companyId, ApplyDescriptionRequest request);
}
