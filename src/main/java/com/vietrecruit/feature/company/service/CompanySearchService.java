package com.vietrecruit.feature.company.service;

import com.vietrecruit.common.response.SearchPageResponse;
import com.vietrecruit.feature.company.dto.request.CompanySearchRequest;
import com.vietrecruit.feature.company.dto.response.CompanySearchResponse;

public interface CompanySearchService {

    /**
     * Executes a full-text company search against Elasticsearch using the provided filters.
     *
     * @param request search parameters including name keyword, industry, and pagination
     * @return paginated search results with relevance scores
     */
    SearchPageResponse<CompanySearchResponse> search(CompanySearchRequest request);
}
