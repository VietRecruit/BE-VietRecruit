package com.vietrecruit.feature.location.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vietrecruit.feature.location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    Page<Location> findByCompanyId(UUID companyId, Pageable pageable);

    Optional<Location> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByCompanyIdAndName(UUID companyId, String name);

    boolean existsByCompanyIdAndNameAndIdNot(UUID companyId, String name, UUID excludeId);
}
