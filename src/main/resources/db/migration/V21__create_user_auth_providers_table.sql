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
