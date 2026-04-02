package com.vietrecruit.feature.candidate.service;

import com.vietrecruit.common.response.SearchPageResponse;
import com.vietrecruit.feature.candidate.dto.request.CandidateSearchRequest;
import com.vietrecruit.feature.candidate.dto.response.CandidateSearchResponse;

public interface CandidateSearchService {

    /**
     * Executes a full-text candidate search against Elasticsearch using the provided filters.
     *
     * @param request search parameters including keywords, experience range, and pagination
     * @return paginated search results with relevance scores
     */
    SearchPageResponse<CandidateSearchResponse> search(CandidateSearchRequest request);
}
