-- Add version columns for optimistic locking (JPA @Version)

ALTER TABLE applications ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE offers ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE employer_subscriptions ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
