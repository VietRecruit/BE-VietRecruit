package com.vietrecruit.feature.ai.interview.service;

import java.util.UUID;

import com.vietrecruit.feature.ai.interview.dto.InterviewQuestionResponse;

public interface InterviewQuestionService {

    /**
     * Generates interview questions for the given interview using the AI model. Idempotent —
     * returns stored questions if already generated without invoking OpenAI again.
     *
     * @param interviewId the target interview's UUID
     * @param companyId the owning company's UUID
     * @return the generated or cached interview question response
     */
    InterviewQuestionResponse generate(UUID interviewId, UUID companyId);

    /**
     * Returns previously generated questions for the given interview, or throws 404 if not yet
     * generated.
     *
     * @param interviewId the target interview's UUID
     * @param companyId the owning company's UUID
     * @return the stored interview question response
     */
    InterviewQuestionResponse getQuestions(UUID interviewId, UUID companyId);
}
