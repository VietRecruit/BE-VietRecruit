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
