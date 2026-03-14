package com.vietrecruit.common.security.oauth2;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.vietrecruit.feature.auth.dto.response.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationCodeStore {

    private static final String KEY_PREFIX = "oauth2:code:";
    private static final long CODE_TTL_SECONDS = 60;

    private final StringRedisTemplate redisTemplate;

    /** Stores tokens under a one-time authorization code in Redis. Returns the generated code. */
    public String storeTokens(LoginResponse tokens) {
        String code = UUID.randomUUID().toString();
        String key = KEY_PREFIX + code;

        Map<String, String> fields =
                Map.of(
                        "accessToken", tokens.getAccessToken(),
                        "refreshToken", tokens.getRefreshToken(),
                        "expiresIn", String.valueOf(tokens.getExpiresIn()));

        redisTemplate.opsForHash().putAll(key, fields);
        redisTemplate.expire(key, CODE_TTL_SECONDS, TimeUnit.SECONDS);

        log.debug("Stored OAuth2 authorization code: {}", code);
        return code;
    }

    /**
     * Exchanges a one-time authorization code for tokens. The code is deleted after retrieval
     * (one-time use).
     *
     * @return the tokens if the code is valid; empty if expired or already used
     */
    public Optional<LoginResponse> exchangeCode(String code) {
        String key = KEY_PREFIX + code;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        if (entries.isEmpty()) {
            return Optional.empty();
        }

        // Delete immediately (one-time use)
        redisTemplate.delete(key);

        return Optional.of(
                LoginResponse.builder()
                        .accessToken((String) entries.get("accessToken"))
                        .refreshToken((String) entries.get("refreshToken"))
                        .expiresIn(Long.parseLong((String) entries.get("expiresIn")))
                        .build());
    }
}
