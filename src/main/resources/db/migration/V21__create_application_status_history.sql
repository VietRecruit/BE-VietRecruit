-- ============================================================
-- VietRecruit | Migration V21__create_application_status_history.sql
-- Description: Append-only audit trail of application status
--              transitions
-- Depends on:  applications, users
-- ============================================================

CREATE TABLE application_status_history (
    -- Primary key
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign keys
    application_id UUID NOT NULL,
    changed_by     UUID,

    -- Business columns
    old_status     application_status,
    new_status     application_status NOT NULL,
    notes          TEXT,
    changed_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Named constraints
    CONSTRAINT fk_app_history_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE RESTRICT,
    CONSTRAINT fk_app_history_changed_by  FOREIGN KEY (changed_by)     REFERENCES users(id)        ON DELETE SET NULL
);

CREATE INDEX idx_app_status_history_application_id ON application_status_history(application_id);

COMMENT ON TABLE application_status_history IS 'Immutable log of every status change with HR notes';
