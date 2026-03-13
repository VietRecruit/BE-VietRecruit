package com.vietrecruit.feature.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.application.entity.Offer;
import com.vietrecruit.feature.application.enums.OfferStatus;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    Optional<Offer> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByApplicationIdAndStatusInAndDeletedAtIsNull(
            UUID applicationId, List<OfferStatus> statuses);

    List<Offer> findByApplicationIdAndDeletedAtIsNull(UUID applicationId);
}
