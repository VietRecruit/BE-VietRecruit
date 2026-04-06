package com.vietrecruit.feature.payment.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vietrecruit.common.exception.ApiException;
import com.vietrecruit.feature.payment.entity.PaymentTransaction;
import com.vietrecruit.feature.payment.repository.PaymentTransactionRepository;
import com.vietrecruit.feature.subscription.service.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Extracted from PaymentServiceImpl to break self-invocation AOP bypass. All subscription
 * activation calls go through this bean so REQUIRES_NEW propagation is honored by the Spring proxy.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionActivationService {

    private static final int MAX_ACTIVATION_ATTEMPTS = 3;

    private final SubscriptionService subscriptionService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    // Each activation runs in its own transaction — outer batch failure does not roll back prior
    // items
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void tryActivateSubscription(PaymentTransaction tx) {
        if (tx.getActivationAttempts() >= MAX_ACTIVATION_ATTEMPTS) {
            log.warn(
                    "Skipping activation for orderCode={}: max attempts ({}) reached",
                    tx.getOrderCode(),
                    MAX_ACTIVATION_ATTEMPTS);
            return;
        }

        tx.setActivationAttempts(tx.getActivationAttempts() + 1);

        try {
            subscriptionService.activateSubscription(
                    tx.getCompanyId(), tx.getPlan(), tx.getBillingCycle());
        } catch (DataIntegrityViolationException | ApiException | IllegalArgumentException e) {
            // Permanent error — persist attempt count and stop retrying
            log.error(
                    "Subscription activation permanently failed for orderCode={}, company={}"
                            + " (attempt {}/{}): {}",
                    tx.getOrderCode(),
                    tx.getCompanyId(),
                    tx.getActivationAttempts(),
                    MAX_ACTIVATION_ATTEMPTS,
                    e.getMessage());
            paymentTransactionRepository.save(tx);
            return;
        } catch (Exception e) {
            // Transient error — persist attempt count and re-throw for retry
            log.error(
                    "Subscription activation transiently failed for orderCode={}, company={}"
                            + " (attempt {}/{}). Recovery job will retry. Error: {}",
                    tx.getOrderCode(),
                    tx.getCompanyId(),
                    tx.getActivationAttempts(),
                    MAX_ACTIVATION_ATTEMPTS,
                    e.getMessage(),
                    e);
            paymentTransactionRepository.save(tx);
            throw e;
        }

        paymentTransactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void activateSubscription(PaymentTransaction tx) {
        subscriptionService.activateSubscription(
                tx.getCompanyId(), tx.getPlan(), tx.getBillingCycle());
    }
}
