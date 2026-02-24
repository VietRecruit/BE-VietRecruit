package com.vietrecruit.feature.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.auth.entity.UserAuthProvider;

@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, UUID> {

    Optional<UserAuthProvider> findByUserIdAndProvider(UUID userId, String provider);

    Optional<UserAuthProvider> findByProviderAndProviderUserId(
            String provider, String providerUserId);
}
