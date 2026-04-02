package com.vietrecruit.feature.location.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vietrecruit.feature.location.dto.request.LocationRequest;
import com.vietrecruit.feature.location.dto.response.LocationResponse;

public interface LocationService {

    /**
     * Creates a new job location for the given company.
     *
     * @param companyId the owning company's UUID
     * @param userId UUID of the user creating the location
     * @param request location name and address fields
     * @return the created location response
     */
    LocationResponse createLocation(UUID companyId, UUID userId, LocationRequest request);

    /**
     * Updates the fields of an existing location owned by the company.
     *
     * @param companyId the owning company's UUID
     * @param locationId the target location's UUID
     * @param userId UUID of the user performing the update
     * @param request updated location fields
     * @return the updated location response
     */
    LocationResponse updateLocation(
            UUID companyId, UUID locationId, UUID userId, LocationRequest request);

    /**
     * Returns a single location owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param locationId the target location's UUID
     * @return the location response
     */
    LocationResponse getLocation(UUID companyId, UUID locationId);

    /**
     * Returns a paginated list of all locations for the given company.
     *
     * @param companyId the owning company's UUID
     * @param pageable pagination and sort parameters
     * @return page of location responses
     */
    Page<LocationResponse> listLocations(UUID companyId, Pageable pageable);

    /**
     * Deletes a location owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param locationId the target location's UUID
     */
    void deleteLocation(UUID companyId, UUID locationId);
}
