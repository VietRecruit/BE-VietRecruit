package com.vietrecruit.feature.candidate.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.vietrecruit.feature.candidate.dto.request.CandidateUpdateRequest;
import com.vietrecruit.feature.candidate.dto.response.CandidateProfileResponse;
import com.vietrecruit.feature.candidate.dto.response.CvUploadResponse;

public interface CandidateService {

    CandidateProfileResponse getProfile(UUID userId);

    CandidateProfileResponse updateProfile(UUID userId, CandidateUpdateRequest request);

    CvUploadResponse uploadCv(UUID userId, MultipartFile file);

    void deleteCv(UUID userId);

    CandidateProfileResponse getById(UUID candidateId);
}
