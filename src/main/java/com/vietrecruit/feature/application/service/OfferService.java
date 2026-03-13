package com.vietrecruit.feature.application.service;

import java.util.List;
import java.util.UUID;

import com.vietrecruit.feature.application.dto.request.OfferCreateRequest;
import com.vietrecruit.feature.application.dto.request.OfferRespondRequest;
import com.vietrecruit.feature.application.dto.response.OfferResponse;

public interface OfferService {

    OfferResponse createOffer(
            UUID applicationId, UUID companyId, UUID createdBy, OfferCreateRequest request);

    List<OfferResponse> listOffers(UUID applicationId, UUID userId);

    OfferResponse getOffer(UUID offerId, UUID userId);

    OfferResponse sendOffer(UUID offerId, UUID companyId, UUID userId);

    OfferResponse respondToOffer(UUID offerId, UUID userId, OfferRespondRequest request);

    void deleteOffer(UUID offerId, UUID companyId);
}
