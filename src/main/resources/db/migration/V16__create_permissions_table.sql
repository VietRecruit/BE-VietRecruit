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
