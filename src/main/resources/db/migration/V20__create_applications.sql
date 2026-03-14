-- ============================================================
-- VietRecruit | Migration V20__create_applications.sql
-- Description: Candidate job applications
-- Depends on:  jobs, candidates
-- ============================================================

CREATE TABLE applications (
    -- Primary key
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign keys
    job_id         UUID NOT NULL,
    candidate_id   UUID NOT NULL,

    -- Business columns
    applied_cv_url VARCHAR(255)       NOT NULL,
    cover_letter   TEXT,
    status         application_status NOT NULL DEFAULT 'NEW',

    -- Audit columns
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMPTZ,

    -- Named constraints
    CONSTRAINT fk_applications_job       FOREIGN KEY (job_id)       REFERENCES jobs(id)       ON DELETE RESTRICT,
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE RESTRICT,
    CONSTRAINT uq_applications_job_candidate UNIQUE (job_id, candidate_id)
);

-- Indexes
CREATE INDEX idx_applications_job_id       ON applications(job_id);
CREATE INDEX idx_applications_candidate_id ON applications(candidate_id);
CREATE INDEX idx_applications_status       ON applications(status);

COMMENT ON TABLE  applications                IS 'One application per candidate per job';
COMMENT ON COLUMN applications.applied_cv_url IS 'Snapshot of CV at time of application (immutable)';
