-- V1__init_extensions_and_enums.sql
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE user_role AS ENUM ('SYSTEM_ADMIN', 'COMPANY_ADMIN', 'HR', 'INTERVIEWER', 'CANDIDATE');
CREATE TYPE job_status AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED');
CREATE TYPE application_status AS ENUM ('NEW', 'SCREENING', 'INTERVIEW', 'OFFER', 'HIRED', 'REJECTED');
CREATE TYPE interview_status AS ENUM ('SCHEDULED', 'COMPLETED', 'CANCELED');
CREATE TYPE scorecard_result AS ENUM ('PASS', 'FAIL', 'CONSIDERING');
CREATE TYPE offer_status AS ENUM ('DRAFT', 'SENT', 'ACCEPTED', 'DECLINED');


-- V2__create_companies_table.sql
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255) UNIQUE,
    website VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);


-- V3__create_users_table.sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID REFERENCES companies(id),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    avatar_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    location VARCHAR(255),
    dob DATE,
    gender VARCHAR(20),
    role user_role DEFAULT 'CANDIDATE',
    email_verified BOOLEAN DEFAULT FALSE,
    email_verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_company_id ON users(company_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_email_verified ON users(id) WHERE email_verified = FALSE;


-- V4__create_departments_table.sql
CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_departments_company_id ON departments(company_id);


-- V5__create_locations_table.sql
CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    address TEXT,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_locations_company_id ON locations(company_id);


-- V6__create_categories_table.sql
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_company_id ON categories(company_id);


-- V7__create_jobs_table.sql
CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    department_id UUID REFERENCES departments(id),
    location_id UUID REFERENCES locations(id),
    category_id UUID REFERENCES categories(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT,
    min_salary NUMERIC(15, 2),
    max_salary NUMERIC(15, 2),
    currency VARCHAR(10) DEFAULT 'VND',
    is_negotiable BOOLEAN DEFAULT FALSE,
    status job_status DEFAULT 'DRAFT',
    deadline DATE,
    public_link VARCHAR(255) UNIQUE,
    embedding vector(1536),
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_department_id ON jobs(department_id);
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_deleted_at ON jobs(deleted_at);
CREATE INDEX hnsw_job_embedding_idx ON jobs USING hnsw (embedding vector_cosine_ops);


-- V8__create_candidates_table.sql
CREATE TABLE candidates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    headline VARCHAR(255),
    summary TEXT,
    default_cv_url VARCHAR(255),
    cv_embedding vector(1536),
    parsed_cv_text TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_candidates_user_id ON candidates(user_id);
CREATE INDEX hnsw_candidate_cv_idx ON candidates USING hnsw (cv_embedding vector_cosine_ops);


-- V9__create_applications_table.sql
CREATE TABLE applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id UUID NOT NULL REFERENCES jobs(id),
    candidate_id UUID NOT NULL REFERENCES candidates(id),
    applied_cv_url VARCHAR(255) NOT NULL,
    cover_letter TEXT,
    status application_status DEFAULT 'NEW',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    UNIQUE(job_id, candidate_id)
);

CREATE INDEX idx_applications_job_id ON applications(job_id);
CREATE INDEX idx_applications_candidate_id ON applications(candidate_id);
CREATE INDEX idx_applications_status ON applications(status);


-- V10__create_application_status_history_table.sql
CREATE TABLE application_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL REFERENCES applications(id),
    old_status application_status,
    new_status application_status NOT NULL,
    notes TEXT,
    changed_by UUID REFERENCES users(id),
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_application_status_history_application_id ON application_status_history(application_id);


-- V11__create_interviews_table.sql
CREATE TABLE interviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL REFERENCES applications(id),
    title VARCHAR(255) NOT NULL,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    duration_minutes INTEGER DEFAULT 60,
    location_or_link VARCHAR(255),
    interview_type VARCHAR(50),
    status interview_status DEFAULT 'SCHEDULED',
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_interviews_application_id ON interviews(application_id);
CREATE INDEX idx_interviews_status ON interviews(status);


-- V12__create_interview_interviewers_table.sql
CREATE TABLE interview_interviewers (
    interview_id UUID NOT NULL REFERENCES interviews(id),
    user_id UUID NOT NULL REFERENCES users(id),
    PRIMARY KEY (interview_id, user_id)
);


-- V13__create_scorecards_table.sql
CREATE TABLE scorecards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    interview_id UUID NOT NULL REFERENCES interviews(id),
    interviewer_id UUID NOT NULL REFERENCES users(id),
    skill_score NUMERIC(5, 2) CHECK (skill_score >= 0),
    attitude_score NUMERIC(5, 2) CHECK (attitude_score >= 0),
    english_score NUMERIC(5, 2) CHECK (english_score >= 0),
    average_score NUMERIC(5, 2) GENERATED ALWAYS AS ((skill_score + attitude_score + english_score) / 3.0) STORED,
    comments TEXT,
    result scorecard_result NOT NULL,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_scorecards_interview_id ON scorecards(interview_id);


-- V14__create_offers_table.sql
CREATE TABLE offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL REFERENCES applications(id),
    offer_letter_url VARCHAR(255),
    base_salary NUMERIC(15, 2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'VND',
    start_date DATE,
    note TEXT,
    status offer_status DEFAULT 'DRAFT',
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_offers_application_id ON offers(application_id);


-- V15__create_roles_table.sql
CREATE TABLE roles (
    id         SMALLSERIAL PRIMARY KEY,
    code       VARCHAR(50) UNIQUE NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Seed roles
INSERT INTO roles (code, name) VALUES
    ('SYSTEM_ADMIN', 'System Administrator'),
    ('COMPANY_ADMIN', 'Company Administrator'),
    ('HR', 'Human Resources'),
    ('INTERVIEWER', 'Interviewer'),
    ('CANDIDATE', 'Candidate');


-- V16__create_permissions_table.sql
CREATE TABLE permissions (
    id         SMALLSERIAL PRIMARY KEY,
    code       VARCHAR(100) UNIQUE NOT NULL,
    name       VARCHAR(150) NOT NULL,
    module     VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_permissions_module ON permissions(module);

-- Seed permissions
INSERT INTO permissions (code, name, module) VALUES
    -- Job
    ('JOB:CREATE', 'Create Job Posting', 'JOB'),
    ('JOB:VIEW', 'View Job Posting', 'JOB'),
    ('JOB:EDIT', 'Edit Job Posting', 'JOB'),
    ('JOB:DELETE', 'Delete Job Posting', 'JOB'),
    -- Candidate
    ('CANDIDATE:VIEW', 'View Candidate Profile', 'CANDIDATE'),
    ('CANDIDATE:EDIT', 'Edit Candidate Profile', 'CANDIDATE'),
    -- Application
    ('APPLICATION:VIEW', 'View Application', 'APPLICATION'),
    ('APPLICATION:MANAGE', 'Manage Application Status', 'APPLICATION'),
    -- Interview
    ('INTERVIEW:SCHEDULE', 'Schedule Interview', 'INTERVIEW'),
    ('INTERVIEW:VIEW', 'View Interview', 'INTERVIEW'),
    -- Offer
    ('OFFER:CREATE', 'Create Offer', 'OFFER'),
    ('OFFER:VIEW', 'View Offer', 'OFFER'),
    -- Company
    ('COMPANY:MANAGE', 'Manage Company Settings', 'COMPANY'),
    -- User
    ('USER:MANAGE', 'Manage Users', 'USER');


-- V17__create_role_permissions_table.sql
CREATE TABLE role_permissions (
    role_id       SMALLINT REFERENCES roles(id) ON DELETE CASCADE,
    permission_id SMALLINT REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Seed role-permission assignments
-- SYSTEM_ADMIN (id=1): all permissions
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 1, id FROM permissions;

-- COMPANY_ADMIN (id=2): company management + user management + all operational
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 2, id FROM permissions
    WHERE code IN (
        'JOB:CREATE', 'JOB:VIEW', 'JOB:EDIT', 'JOB:DELETE',
        'CANDIDATE:VIEW', 'CANDIDATE:EDIT',
        'APPLICATION:VIEW', 'APPLICATION:MANAGE',
        'INTERVIEW:SCHEDULE', 'INTERVIEW:VIEW',
        'OFFER:CREATE', 'OFFER:VIEW',
        'COMPANY:MANAGE', 'USER:MANAGE'
    );

-- HR (id=3): job + candidate + application + interview + offer management
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 3, id FROM permissions
    WHERE code IN (
        'JOB:CREATE', 'JOB:VIEW', 'JOB:EDIT', 'JOB:DELETE',
        'CANDIDATE:VIEW', 'CANDIDATE:EDIT',
        'APPLICATION:VIEW', 'APPLICATION:MANAGE',
        'INTERVIEW:SCHEDULE', 'INTERVIEW:VIEW',
        'OFFER:CREATE', 'OFFER:VIEW'
    );

-- INTERVIEWER (id=4): view jobs, view candidates, view/manage interviews
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 4, id FROM permissions
    WHERE code IN (
        'JOB:VIEW',
        'CANDIDATE:VIEW',
        'APPLICATION:VIEW',
        'INTERVIEW:VIEW',
        'INTERVIEW:SCHEDULE'
    );

-- CANDIDATE (id=5): view jobs, view own applications, view own interviews, view own offers
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 5, id FROM permissions
    WHERE code IN (
        'JOB:VIEW',
        'APPLICATION:VIEW',
        'INTERVIEW:VIEW',
        'OFFER:VIEW'
    );


-- V18__create_user_roles_table.sql
CREATE TABLE user_roles (
    user_id UUID     REFERENCES users(id) ON DELETE CASCADE,
    role_id SMALLINT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);


-- V19__migrate_user_role_to_rbac.sql
-- Migrate existing users.role enum values to user_roles table
INSERT INTO user_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u
    JOIN roles r ON r.code = u.role::TEXT
    WHERE u.role IS NOT NULL
    ON CONFLICT DO NOTHING;

-- Drop the enum column
ALTER TABLE users DROP COLUMN IF EXISTS role;

-- Drop the enum type
DROP TYPE IF EXISTS user_role;

-- Add authentication-related columns
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_active       BOOLEAN  DEFAULT TRUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_locked       BOOLEAN  DEFAULT FALSE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS failed_attempts SMALLINT DEFAULT 0;
ALTER TABLE users ADD COLUMN IF NOT EXISTS lock_until      TIMESTAMPTZ;
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_login_at   TIMESTAMPTZ;

-- Partial index for active user queries
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active) WHERE deleted_at IS NULL;


-- V20__create_refresh_tokens_table.sql
CREATE TABLE refresh_tokens (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash  VARCHAR(64) NOT NULL,
    device_info VARCHAR(255),
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked     BOOLEAN DEFAULT FALSE,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_tokens_expires ON refresh_tokens(expires_at) WHERE revoked = FALSE;


-- V21__create_user_auth_providers_table.sql
CREATE TABLE user_auth_providers (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    provider            VARCHAR(20)  NOT NULL,
    provider_user_id    VARCHAR(255) NOT NULL,
    provider_email      VARCHAR(255),
    provider_name       VARCHAR(255),
    provider_avatar_url VARCHAR(512),
    linked_at           TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_user_provider UNIQUE (user_id, provider),
    CONSTRAINT uq_provider_user UNIQUE (provider, provider_user_id)
);

CREATE INDEX idx_uap_user_id ON user_auth_providers(user_id);
CREATE INDEX idx_uap_provider_lookup ON user_auth_providers(provider, provider_user_id);


-- V22__create_subscription_plans_table.sql
-- Subscription status enum
CREATE TYPE subscription_status AS ENUM ('ACTIVE', 'EXPIRED', 'CANCELLED');

-- Subscription plans catalog
CREATE TABLE subscription_plans (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code              VARCHAR(50) UNIQUE NOT NULL,
    name              VARCHAR(100) NOT NULL,
    description       TEXT,
    max_active_jobs   INTEGER NOT NULL DEFAULT 1,
    job_duration_days INTEGER NOT NULL DEFAULT 30,
    resume_access     BOOLEAN DEFAULT FALSE,
    ai_matching       BOOLEAN DEFAULT FALSE,
    priority_listing  BOOLEAN DEFAULT FALSE,
    price_monthly     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    price_yearly      NUMERIC(12, 2),
    currency          VARCHAR(10) DEFAULT 'VND',
    is_active         BOOLEAN DEFAULT TRUE,
    created_at        TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Seed default plans (-1 = unlimited)
INSERT INTO subscription_plans (code, name, description, max_active_jobs, job_duration_days, resume_access, ai_matching, priority_listing, price_monthly) VALUES
    ('FREE',       'Free',       'Get started with basic job posting',            1,  15, FALSE, FALSE, FALSE, 0),
    ('BASIC',      'Basic',      'Essential tools for growing teams',             5,  30, TRUE,  FALSE, FALSE, 500000),
    ('PREMIUM',    'Premium',    'Advanced features for active recruiters',       20, 60, TRUE,  TRUE,  TRUE,  1500000),
    ('DEV', 'Dev', 'Unlimited access for large-scale hiring',       -1, 90, TRUE,  TRUE,  TRUE,  10000),
    ('ENTERPRISE', 'Enterprise', 'Unlimited access for large-scale hiring',       -1, 90, TRUE,  TRUE,  TRUE,  5000000);


-- V23__create_employer_subscriptions_table.sql
CREATE TABLE employer_subscriptions (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id   UUID NOT NULL REFERENCES companies(id),
    plan_id      UUID NOT NULL REFERENCES subscription_plans(id),
    status       subscription_status NOT NULL DEFAULT 'ACTIVE',
    started_at   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at   TIMESTAMPTZ NOT NULL,
    cancelled_at TIMESTAMPTZ,
    auto_renew   BOOLEAN DEFAULT TRUE,
    payment_ref  VARCHAR(255),
    created_at   TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employer_sub_company ON employer_subscriptions(company_id);
CREATE INDEX idx_employer_sub_status ON employer_subscriptions(status) WHERE status = 'ACTIVE';
CREATE INDEX idx_employer_sub_expires ON employer_subscriptions(expires_at) WHERE status = 'ACTIVE';

-- One active subscription per company at any time
CREATE UNIQUE INDEX uq_employer_sub_active ON employer_subscriptions(company_id) WHERE status = 'ACTIVE';


-- V24__create_job_posting_quotas_table.sql
CREATE TABLE job_posting_quotas (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscription_id UUID NOT NULL REFERENCES employer_subscriptions(id),
    jobs_posted     INTEGER NOT NULL DEFAULT 0,
    jobs_active     INTEGER NOT NULL DEFAULT 0,
    cycle_start     TIMESTAMPTZ NOT NULL,
    cycle_end       TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uq_quota_subscription ON job_posting_quotas(subscription_id);


-- V25__seed_subscription_permissions.sql
-- Subscription-related permissions
INSERT INTO permissions (code, name, module) VALUES
    ('PLAN:VIEW',            'View Plans',              'PLAN'),
    ('PLAN:MANAGE',          'Manage Plans',            'PLAN'),
    ('SUBSCRIPTION:VIEW',    'View Subscriptions',      'SUBSCRIPTION'),
    ('SUBSCRIPTION:MANAGE',  'Manage Subscriptions',    'SUBSCRIPTION');

-- SYSTEM_ADMIN (id=1): grant all new permissions
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 1, id FROM permissions WHERE module IN ('PLAN', 'SUBSCRIPTION');

-- COMPANY_ADMIN (id=2): view plans + view/manage own subscription
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 2, id FROM permissions WHERE code IN ('PLAN:VIEW', 'SUBSCRIPTION:VIEW', 'SUBSCRIPTION:MANAGE');

-- HR (id=3): view plans + view subscription
INSERT INTO role_permissions (role_id, permission_id)
    SELECT 3, id FROM permissions WHERE code IN ('PLAN:VIEW', 'SUBSCRIPTION:VIEW');


-- V26__create_payment_transactions_table.sql
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


-- V27__add_billing_cycle_to_employer_subscriptions.sql
-- Add billing cycle column to employer_subscriptions
ALTER TABLE employer_subscriptions ADD COLUMN billing_cycle billing_cycle DEFAULT 'MONTHLY';


-- V28__seed_companies_data.sql
-- Seed default companies for testing and development

INSERT INTO companies (id, name, domain, website)
VALUES
    ('17f1e620-e7eb-49e5-a692-84f9c4af5b1e', 'VietRecruit HQ', 'vietrecruit.com', 'https://vietrecruit.com'),
    ('f7b7bfe5-7221-4b8f-b839-912281a650f5', 'Alpha Tech', 'alpha.tech', 'https://alpha.tech'),
    ('6970e198-65d9-44aa-95a1-98b560f1fb08', 'Beta Solutions', 'beta.solutions', 'https://beta.solutions')
ON CONFLICT (id) DO NOTHING;


-- V29__harden_payment_transactions.sql
-- Add FAILED to payment_status enum
ALTER TYPE payment_status ADD VALUE IF NOT EXISTS 'FAILED';

-- Add audit columns for non-00 webhook responses
ALTER TABLE payment_transactions
    ADD COLUMN failure_code   VARCHAR(10),
    ADD COLUMN failure_reason  VARCHAR(255);

-- Prevent concurrent pending payments per company
CREATE UNIQUE INDEX uq_payment_tx_company_pending
    ON payment_transactions(company_id) WHERE status = 'PENDING';


-- V30__create_transaction_records_table.sql
-- Transaction records table (append-only audit log from PayOS webhook data)
CREATE TABLE transaction_records (
    id                         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_code                 BIGINT NOT NULL REFERENCES payment_transactions(order_code),
    company_id                 UUID NOT NULL REFERENCES companies(id),
    account_number             VARCHAR(50),
    amount                     BIGINT NOT NULL,
    description                TEXT,
    reference                  VARCHAR(100),
    transaction_date_time      TIMESTAMPTZ NOT NULL,
    counter_account_bank_id    VARCHAR(50),
    counter_account_name       VARCHAR(255),
    counter_account_number     VARCHAR(50),
    currency                   VARCHAR(3) NOT NULL DEFAULT 'VND',
    payment_link_id            VARCHAR(100),
    payos_code                 VARCHAR(10) NOT NULL,
    payos_desc                 VARCHAR(255),
    created_at                 TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uq_tx_records_order_code ON transaction_records(order_code);
CREATE INDEX idx_tx_records_company ON transaction_records(company_id);


-- V31__seed_customer_service_role.sql
-- Customer Service role
INSERT INTO roles (code, name) VALUES ('CUSTOMER_SERVICE', 'Customer Service');

-- Payment transaction viewing permission
INSERT INTO permissions (code, name, module) VALUES
    ('TRANSACTION:VIEW_ALL', 'View All Transaction Records', 'PAYMENT');

-- Assign TRANSACTION:VIEW_ALL to CUSTOMER_SERVICE
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'CUSTOMER_SERVICE' AND p.code = 'TRANSACTION:VIEW_ALL';

-- Also assign TRANSACTION:VIEW_ALL to SYSTEM_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.id, p.id FROM roles r, permissions p
    WHERE r.code = 'SYSTEM_ADMIN' AND p.code = 'TRANSACTION:VIEW_ALL';


-- V32__add_implicit_enum_casts.sql
-- Add implicit casts from varchar to all PostgreSQL native enum types.
-- Required because Hibernate 6 with @Enumerated(EnumType.STRING) sends enum values
-- as VARCHAR, but PostgreSQL has no built-in implicit cast from varchar to custom enums.

CREATE CAST (varchar AS payment_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS billing_cycle) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS subscription_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS job_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS application_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS interview_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS scorecard_result) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS offer_status) WITH INOUT AS IMPLICIT;


