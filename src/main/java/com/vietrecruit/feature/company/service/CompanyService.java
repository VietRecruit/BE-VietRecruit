package com.vietrecruit.feature.company.service;

import java.util.UUID;

import com.vietrecruit.feature.company.dto.request.CompanyCreateRequest;
import com.vietrecruit.feature.company.dto.request.CompanyUpdateRequest;
import com.vietrecruit.feature.company.dto.response.CompanyResponse;

public interface CompanyService {

    /**
     * Creates a new company profile associated with the given user.
     *
     * @param userId the user UUID who is registering as a company admin
     * @param request company details including name, industry, and contact information
     * @return the created company response
     */
    CompanyResponse createCompany(UUID userId, CompanyCreateRequest request);

    /**
     * Returns the company profile for the given company UUID.
     *
     * @param companyId the target company's UUID
     * @return the company response
     */
    CompanyResponse getCompany(UUID companyId);

    /**
     * Updates the mutable fields of an existing company profile.
     *
     * @param companyId the target company's UUID
     * @param request updated company fields
     * @return the updated company response
     */
    CompanyResponse updateCompany(UUID companyId, CompanyUpdateRequest request);
}
