package com.vietrecruit.feature.location.service;

import java.util.List;
import java.util.UUID;

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
     * Returns all locations for the given company, ordered by name ascending.
     *
     * @param companyId the owning company's UUID
     * @return list of location responses
     */
    List<LocationResponse> listLocations(UUID companyId);

    /**
     * Deletes a location owned by the given company.
     *
     * @param companyId the owning company's UUID
     * @param locationId the target location's UUID
     */
    void deleteLocation(UUID companyId, UUID locationId);
}
