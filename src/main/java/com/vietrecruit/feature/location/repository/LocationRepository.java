package com.vietrecruit.feature.location.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vietrecruit.feature.location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    List<Location> findByCompanyIdOrderByNameAsc(UUID companyId);

    Optional<Location> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByCompanyIdAndName(UUID companyId, String name);

    boolean existsByCompanyIdAndNameAndIdNot(UUID companyId, String name, UUID excludeId);
}
