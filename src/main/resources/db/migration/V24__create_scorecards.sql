-- ============================================================
-- VietRecruit | Migration V24__create_scorecards.sql
-- Description: Interviewer evaluation scorecards
-- Depends on:  interviews, users
-- ============================================================

CREATE TABLE scorecards (
    -- Primary key
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign keys
    interview_id    UUID NOT NULL,
    interviewer_id  UUID NOT NULL,
    created_by      UUID,
    updated_by      UUID,

    -- Business columns
    skill_score     NUMERIC(5, 2),
    attitude_score  NUMERIC(5, 2),
    english_score   NUMERIC(5, 2),
    average_score   NUMERIC(5, 2) GENERATED ALWAYS AS ((skill_score + attitude_score + english_score) / 3.0) STORED,
    comments        TEXT,
    result          scorecard_result NOT NULL,

    -- Audit columns
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Named constraints
    CONSTRAINT fk_scorecards_interview    FOREIGN KEY (interview_id)   REFERENCES interviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_scorecards_interviewer  FOREIGN KEY (interviewer_id) REFERENCES users(id)      ON DELETE CASCADE,
    CONSTRAINT fk_scorecards_created_by   FOREIGN KEY (created_by)     REFERENCES users(id)      ON DELETE SET NULL,
    CONSTRAINT fk_scorecards_updated_by   FOREIGN KEY (updated_by)     REFERENCES users(id)      ON DELETE SET NULL,
    CONSTRAINT chk_scorecards_skill       CHECK (skill_score >= 0),
    CONSTRAINT chk_scorecards_attitude    CHECK (attitude_score >= 0),
    CONSTRAINT chk_scorecards_english     CHECK (english_score >= 0)
);

CREATE INDEX idx_scorecards_interview_id ON scorecards(interview_id);

COMMENT ON TABLE  scorecards              IS 'Per-interviewer evaluation scores for an interview round';
COMMENT ON COLUMN scorecards.average_score IS 'Auto-computed average of the three score dimensions';
