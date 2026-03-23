package com.vietrecruit.feature.ai.shared.tools;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.vietrecruit.common.security.SecurityUtils;
import com.vietrecruit.feature.candidate.dto.response.CandidateSearchResult;
import com.vietrecruit.feature.candidate.service.CandidateService;
import com.vietrecruit.feature.user.entity.User;
import com.vietrecruit.feature.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandidateSearchTool {

    private final CandidateService candidateService;
    private final UserRepository userRepository;

    @Tool(
            description =
                    "Search candidates by skills, experience years, location, and availability."
                            + " Use when employers need to find matching talent.")
    public String searchCandidates(
            String skills, String minYearsExperience, String desiredPosition) {

        UUID userId = SecurityUtils.getCurrentUserId();
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new IllegalStateException("Authenticated user not found"));
        UUID companyId = user.getCompanyId();
        if (companyId == null) {
            log.warn("CandidateSearchTool: authenticated user has no company: userId={}", userId);
            return "No company associated with this account.";
        }

        Short minYears = null;
        if (minYearsExperience != null && !minYearsExperience.isBlank()) {
            try {
                minYears = Short.parseShort(minYearsExperience);
            } catch (NumberFormatException ignored) {
            }
        }

        List<CandidateSearchResult> results =
                candidateService.searchCandidatesForCompany(
                        skills, desiredPosition, minYears, 10, companyId);

        if (results.isEmpty()) {
            return "No matching candidates found.";
        }

        StringBuilder sb =
                new StringBuilder("Found ").append(results.size()).append(" candidates:\n");
        for (CandidateSearchResult c : results) {
            sb.append("- [")
                    .append(c.id())
                    .append("] ")
                    .append(c.desiredPosition() != null ? c.desiredPosition() : "N/A");
            if (c.yearsOfExperience() != null) {
                sb.append(" | ").append(c.yearsOfExperience()).append(" yrs exp");
            }
            if (c.skills() != null) {
                sb.append(" | Skills: ").append(String.join(", ", c.skills()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
