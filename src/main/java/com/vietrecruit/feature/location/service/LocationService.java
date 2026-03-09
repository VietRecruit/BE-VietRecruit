package com.vietrecruit.feature.location.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.location.dto.request.LocationRequest;
import com.vietrecruit.feature.location.dto.response.LocationResponse;

public interface LocationService {

    LocationResponse createLocation(UUID companyId, UUID userId, LocationRequest request);

    LocationResponse updateLocation(
            UUID companyId, UUID locationId, UUID userId, LocationRequest request);

    LocationResponse getLocation(UUID companyId, UUID locationId);

    Page<LocationResponse> listLocations(UUID companyId, Pageable pageable);

    void deleteLocation(UUID companyId, UUID locationId);
}
