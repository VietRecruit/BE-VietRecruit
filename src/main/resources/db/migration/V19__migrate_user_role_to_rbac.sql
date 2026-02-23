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
