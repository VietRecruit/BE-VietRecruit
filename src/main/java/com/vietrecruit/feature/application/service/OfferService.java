package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.request.OfferCreateRequest;
import com.vietrecruit.feature.application.dto.request.OfferRespondRequest;
import com.vietrecruit.feature.application.dto.response.OfferResponse;

public interface OfferService {

    /**
     * Creates a draft offer for an application, enforcing company ownership constraints.
     *
     * @param applicationId the application receiving the offer
     * @param companyId the company issuing the offer
     * @param createdBy UUID of the user creating the offer
     * @param request offer terms including salary and start date
     * @return the created offer response
     */
    OfferResponse createOffer(
            UUID applicationId, UUID companyId, UUID createdBy, OfferCreateRequest request);

    /**
     * Returns all offers for an application visible to the requesting user.
     *
     * @param applicationId the target application's UUID
     * @param userId the requesting user's UUID
     * @return list of offer responses
     */
    List<OfferResponse> listOffers(UUID applicationId, UUID userId);

    /**
     * Returns a single offer visible to the requesting user.
     *
     * @param offerId the target offer's UUID
     * @param userId the requesting user's UUID
     * @return the offer response
     */
    OfferResponse getOffer(UUID offerId, UUID userId);

    /**
     * Sends a draft offer to the candidate, transitioning it to SENT status.
     *
     * @param offerId the target offer's UUID
     * @param companyId the owning company's UUID
     * @param userId the user triggering the send action
     * @return the updated offer response
     */
    OfferResponse sendOffer(UUID offerId, UUID companyId, UUID userId);

    /**
     * Records the candidate's acceptance or rejection of a sent offer.
     *
     * @param offerId the target offer's UUID
     * @param userId the candidate's UUID
     * @param request acceptance or rejection decision with optional message
     * @return the updated offer response
     */
    OfferResponse respondToOffer(UUID offerId, UUID userId, OfferRespondRequest request);

    /**
     * Permanently deletes a draft offer owned by the given company.
     *
     * @param offerId the target offer's UUID
     * @param companyId the owning company's UUID
     */
    void deleteOffer(UUID offerId, UUID companyId);
}
