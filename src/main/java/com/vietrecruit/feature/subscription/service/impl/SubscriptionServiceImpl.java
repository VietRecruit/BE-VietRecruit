package com.vietrecruit.feature.subscription.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vietrecruit.common.enums.ApiErrorCode;
import com.vietrecruit.common.enums.BillingCycle;
import com.vietrecruit.common.exception.ApiException;
import com.vietrecruit.feature.subscription.dto.response.QuotaResponse;
import com.vietrecruit.feature.subscription.dto.response.SubscriptionResponse;
import com.vietrecruit.feature.subscription.entity.EmployerSubscription;
import com.vietrecruit.feature.subscription.entity.JobPostingQuota;
import com.vietrecruit.feature.subscription.entity.SubscriptionPlan;
import com.vietrecruit.feature.subscription.enums.SubscriptionStatus;
import com.vietrecruit.feature.subscription.mapper.SubscriptionMapper;
import com.vietrecruit.feature.subscription.repository.EmployerSubscriptionRepository;
import com.vietrecruit.feature.subscription.repository.JobPostingQuotaRepository;
import com.vietrecruit.feature.subscription.repository.SubscriptionPlanRepository;
import com.vietrecruit.feature.subscription.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final int DEFAULT_BILLING_DAYS = 30;
    private static final int YEARLY_BILLING_DAYS = 365;

    private final SubscriptionPlanRepository planRepository;
    private final EmployerSubscriptionRepository subscriptionRepository;
    private final JobPostingQuotaRepository quotaRepository;
    private final SubscriptionMapper mapper;

    @Override
    @Transactional
    public SubscriptionResponse activateSubscription(
            UUID companyId, SubscriptionPlan plan, BillingCycle cycle) {
        var now = Instant.now();
        int billingDays = cycle == BillingCycle.YEARLY ? YEARLY_BILLING_DAYS : DEFAULT_BILLING_DAYS;
        var expiresAt = now.plus(billingDays, ChronoUnit.DAYS);

        var subscription =
                EmployerSubscription.builder()
                        .companyId(companyId)
                        .plan(plan)
                        .status(SubscriptionStatus.ACTIVE)
                        .startedAt(now)
                        .expiresAt(expiresAt)
                        .billingCycle(cycle)
                        .build();

        try {
            subscription = subscriptionRepository.saveAndFlush(subscription);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(ApiErrorCode.SUBSCRIPTION_ALREADY_ACTIVE);
        }

        var quota =
                JobPostingQuota.builder()
                        .subscription(subscription)
                        .jobsPosted(0)
                        .jobsActive(0)
                        .cycleStart(now)
                        .cycleEnd(expiresAt)
                        .build();
        quotaRepository.save(quota);

        return mapper.toSubscriptionResponse(subscription);
    }

    @Override
    public SubscriptionResponse getCurrentSubscription(UUID companyId) {
        var subscription =
                subscriptionRepository
                        .findActiveByCompanyId(companyId, SubscriptionStatus.ACTIVE)
                        .orElseThrow(() -> new ApiException(ApiErrorCode.SUBSCRIPTION_REQUIRED));
        return mapper.toSubscriptionResponse(subscription);
    }

    @Override
    public QuotaResponse getCurrentQuota(UUID companyId) {
        var subscription =
                subscriptionRepository
                        .findActiveByCompanyId(companyId, SubscriptionStatus.ACTIVE)
                        .orElseThrow(() -> new ApiException(ApiErrorCode.SUBSCRIPTION_REQUIRED));

        var quota =
                quotaRepository
                        .findBySubscriptionId(subscription.getId())
                        .orElseThrow(
                                () ->
                                        new ApiException(
                                                ApiErrorCode.INTERNAL_ERROR,
                                                "Quota record missing"));

        return mapper.toQuotaResponse(quota, subscription.getPlan());
    }

    @Override
    @Transactional
    public void cancelSubscription(UUID companyId) {
        var subscription =
                subscriptionRepository
                        .findActiveByCompanyId(companyId, SubscriptionStatus.ACTIVE)
                        .orElseThrow(() -> new ApiException(ApiErrorCode.SUBSCRIPTION_REQUIRED));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(Instant.now());
        subscriptionRepository.save(subscription);
    }
}
