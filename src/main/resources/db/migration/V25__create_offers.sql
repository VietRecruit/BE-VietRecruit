-- ============================================================
-- VietRecruit | Migration V25__create_offers.sql
-- Description: Job offers extended to candidates
-- Depends on:  applications, users
-- ============================================================

CREATE TABLE offers (
    -- Primary key
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign keys
    application_id   UUID NOT NULL,
    created_by       UUID,
    updated_by       UUID,

    -- Business columns
    offer_letter_url VARCHAR(255),
    base_salary      NUMERIC(15, 2) NOT NULL,
    currency         VARCHAR(10) DEFAULT 'VND',
    start_date       DATE,
    note             TEXT,
    status           offer_status NOT NULL DEFAULT 'DRAFT',

    -- Audit columns
    created_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMPTZ,

    -- Named constraints
    CONSTRAINT fk_offers_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    CONSTRAINT fk_offers_created_by  FOREIGN KEY (created_by)     REFERENCES users(id)        ON DELETE SET NULL,
    CONSTRAINT fk_offers_updated_by  FOREIGN KEY (updated_by)     REFERENCES users(id)        ON DELETE SET NULL
);

CREATE INDEX idx_offers_application_id ON offers(application_id);

COMMENT ON TABLE  offers             IS 'Formal job offers linked to accepted applications';
COMMENT ON COLUMN offers.base_salary IS 'Monthly gross salary in specified currency';
