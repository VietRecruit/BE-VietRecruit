-- ============================================================
-- VietRecruit | Migration V22__create_interviews.sql
-- Description: Scheduled interview sessions
-- Depends on:  applications, users
-- ============================================================

CREATE TABLE interviews (
    -- Primary key
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign keys
    application_id   UUID NOT NULL,
    created_by       UUID,
    updated_by       UUID,

    -- Business columns
    title            VARCHAR(255)     NOT NULL,
    scheduled_at     TIMESTAMPTZ      NOT NULL,
    duration_minutes INTEGER          DEFAULT 60,
    location_or_link VARCHAR(255),
    interview_type   VARCHAR(50),
    status           interview_status NOT NULL DEFAULT 'SCHEDULED',

    -- Audit columns
    created_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMPTZ,

    -- Named constraints
    CONSTRAINT fk_interviews_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    CONSTRAINT fk_interviews_created_by  FOREIGN KEY (created_by)     REFERENCES users(id)        ON DELETE SET NULL,
    CONSTRAINT fk_interviews_updated_by  FOREIGN KEY (updated_by)     REFERENCES users(id)        ON DELETE SET NULL
);

-- Indexes
CREATE INDEX idx_interviews_application_id ON interviews(application_id);
CREATE INDEX idx_interviews_status         ON interviews(status);

COMMENT ON TABLE  interviews                  IS 'Interview rounds linked to a specific application';
COMMENT ON COLUMN interviews.location_or_link IS 'Physical address or Google Meet / Zoom URL';
COMMENT ON COLUMN interviews.interview_type   IS 'e.g. TECHNICAL, BEHAVIORAL, CULTURE_FIT';
