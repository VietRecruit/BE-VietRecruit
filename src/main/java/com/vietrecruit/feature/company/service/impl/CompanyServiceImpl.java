package com.vietrecruit.feature.company.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vietrecruit.common.enums.ApiErrorCode;
import com.vietrecruit.common.exception.ApiException;
import com.vietrecruit.feature.company.dto.request.CompanyUpdateRequest;
import com.vietrecruit.feature.company.dto.response.CompanyResponse;
import com.vietrecruit.feature.company.entity.Company;
import com.vietrecruit.feature.company.mapper.CompanyMapper;
import com.vietrecruit.feature.company.repository.CompanyRepository;
import com.vietrecruit.feature.company.service.CompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompany(UUID companyId) {
        return companyMapper.toResponse(findActiveCompany(companyId));
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(UUID companyId, CompanyUpdateRequest request) {
        var company = findActiveCompany(companyId);

        if (request.getDomain() != null
                && companyRepository.existsByDomainAndIdNot(request.getDomain(), companyId)) {
            throw new ApiException(ApiErrorCode.CONFLICT, "Domain is already in use");
        }

        companyMapper.updateEntity(request, company);
        return companyMapper.toResponse(companyRepository.save(company));
    }

    private Company findActiveCompany(UUID companyId) {
        return companyRepository
                .findByIdAndDeletedAtIsNull(companyId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND, "Company not found"));
    }
}
