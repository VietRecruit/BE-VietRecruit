package com.vietrecruit.feature.candidate.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.vietrecruit.feature.candidate.dto.request.CandidateUpdateRequest;
import com.vietrecruit.feature.candidate.dto.response.CandidateProfileResponse;
import com.vietrecruit.feature.candidate.dto.response.CandidateSearchResult;
import com.vietrecruit.feature.candidate.dto.response.CvUploadResponse;
import com.vietrecruit.feature.candidate.entity.Candidate;

public interface CandidateService {

    /**
     * Returns the full profile of the given candidate.
     *
     * @param userId the candidate's user UUID
     * @return the candidate profile response
     */
    CandidateProfileResponse getProfile(UUID userId);

    /**
     * Updates the mutable profile fields of the given candidate.
     *
     * @param userId the candidate's user UUID
     * @param request updated profile fields
     * @return the updated candidate profile response
     */
    CandidateProfileResponse updateProfile(UUID userId, CandidateUpdateRequest request);

    /**
     * Uploads and stores a new CV file for the candidate, replacing any existing CV.
     *
     * @param userId the candidate's user UUID
     * @param file the multipart CV file (PDF or DOCX)
     * @return upload result including the storage URL
     */
    CvUploadResponse uploadCv(UUID userId, MultipartFile file);

    /**
     * Removes the current CV for the candidate and deletes the associated storage object.
     *
     * @param userId the candidate's user UUID
     */
    void deleteCv(UUID userId);

    /**
     * Returns the profile of a candidate by their internal candidate ID.
     *
     * @param candidateId the candidate entity's UUID
     * @return the candidate profile response
     */
    CandidateProfileResponse getById(UUID candidateId);

    /**
     * Returns an active candidate entity by ID, or empty if not found or inactive.
     *
     * @param candidateId the candidate entity's UUID
     * @return Optional containing the candidate, or empty
     */
    Optional<Candidate> findActiveCandidateById(UUID candidateId);

    /**
     * Performs a JPA-based candidate search used internally by AI tool functions.
     *
     * @param skills comma-separated skill keywords to match
     * @param desiredPosition job title keyword to match
     * @param minYearsExperience minimum years of experience filter; null to skip
     * @param limit maximum number of results to return
     * @return list of candidate search results
     */
    List<CandidateSearchResult> searchCandidates(
            String skills, String desiredPosition, Short minYearsExperience, int limit);

    /**
     * Performs a JPA-based candidate search restricted to candidates who have applied to the given
     * company, used internally by AI tool functions.
     *
     * @param skills comma-separated skill keywords to match
     * @param desiredPosition job title keyword to match
     * @param minYearsExperience minimum years of experience filter; null to skip
     * @param limit maximum number of results to return
     * @param companyId restricts results to candidates with applications at this company
     * @return list of candidate search results
     */
    List<CandidateSearchResult> searchCandidatesForCompany(
            String skills,
            String desiredPosition,
            Short minYearsExperience,
            int limit,
            UUID companyId);
}
