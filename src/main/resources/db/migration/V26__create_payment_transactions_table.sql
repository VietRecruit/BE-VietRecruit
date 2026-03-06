-- Payment status enum
CREATE TYPE payment_status AS ENUM ('PENDING', 'PAID', 'CANCELLED', 'EXPIRED');

-- Billing cycle enum
CREATE TYPE billing_cycle AS ENUM ('MONTHLY', 'YEARLY');

-- Order code sequence for PayOS (must be unique long)
CREATE SEQUENCE payos_order_code_seq START WITH 1000000 INCREMENT BY 1;

-- Payment transactions table
CREATE TABLE payment_transactions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_code      BIGINT UNIQUE NOT NULL,
    company_id      UUID NOT NULL REFERENCES companies(id),
    plan_id         UUID NOT NULL REFERENCES subscription_plans(id),
    billing_cycle   billing_cycle NOT NULL DEFAULT 'MONTHLY',
    amount          BIGINT NOT NULL,
    status          payment_status NOT NULL DEFAULT 'PENDING',
    checkout_url    TEXT,
    payos_reference VARCHAR(255),
    paid_at         TIMESTAMPTZ,
    version         BIGINT DEFAULT 0,
    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payment_tx_company ON payment_transactions(company_id);
CREATE INDEX idx_payment_tx_status ON payment_transactions(status) WHERE status = 'PENDING';
CREATE INDEX idx_payment_tx_order_code ON payment_transactions(order_code);
