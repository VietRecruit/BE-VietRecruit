package com.vietrecruit.feature.ai.salary.service;

import java.util.UUID;

import com.vietrecruit.feature.ai.salary.dto.SalaryBenchmarkResponse;

public interface SalaryBenchmarkService {

    /**
     * Returns a salary benchmark estimate based on a free-text job title and location, intended for
     * candidate self-assessment.
     *
     * @param jobTitle the job title to benchmark
     * @param locationId the target location UUID
     * @return salary benchmark response with estimated range and market context
     */
    SalaryBenchmarkResponse benchmarkForCandidate(String jobTitle, UUID locationId);

    /**
     * Returns a salary benchmark estimate for an existing job posting using its stored attributes.
     *
     * @param jobId the target job's UUID
     * @return salary benchmark response with estimated range and market context
     */
    SalaryBenchmarkResponse benchmarkForJob(UUID jobId);
}
