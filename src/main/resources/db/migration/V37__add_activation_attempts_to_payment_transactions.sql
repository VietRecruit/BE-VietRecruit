-- MEDIUM-01: Track activation retry attempts to cap permanent failures at 3
ALTER TABLE payment_transactions
    ADD COLUMN IF NOT EXISTS activation_attempts INTEGER NOT NULL DEFAULT 0;
