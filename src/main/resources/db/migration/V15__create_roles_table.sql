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
