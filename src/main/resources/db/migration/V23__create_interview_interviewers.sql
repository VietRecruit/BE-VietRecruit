-- ============================================================
-- VietRecruit | Migration V23__create_interview_interviewers.sql
-- Description: Junction mapping interviews to interviewer users
-- Depends on:  interviews, users
-- ============================================================

CREATE TABLE interview_interviewers (
    interview_id UUID NOT NULL,
    user_id      UUID NOT NULL,

    PRIMARY KEY (interview_id, user_id),

    CONSTRAINT fk_ii_interview FOREIGN KEY (interview_id) REFERENCES interviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_ii_user      FOREIGN KEY (user_id)      REFERENCES users(id)      ON DELETE CASCADE
);
