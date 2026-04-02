package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.response.ApplicationScreeningResponse;

public interface ScreeningService {

    /**
     * Returns the AI screening results for all applications submitted to the given job.
     *
     * @param jobId the target job's UUID
     * @param companyId the owning company's UUID
     * @return list of screening responses ordered by score
     */
    List<ApplicationScreeningResponse> screenApplications(UUID jobId, UUID companyId);

    /**
     * Enqueues asynchronous AI scoring for all unscored applications on the given job.
     *
     * @param jobId the target job's UUID
     * @param companyId the owning company's UUID
     */
    void triggerAsyncScoring(UUID jobId, UUID companyId);
}
