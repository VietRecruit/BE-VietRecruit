package com.vietrecruit.feature.payment.service;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vietrecruit.feature.payment.entity.PaymentTransaction;
import com.vietrecruit.feature.payment.enums.PaymentStatus;
import com.vietrecruit.feature.payment.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;

/**
 * Expires a single payment in its own REQUIRES_NEW transaction. Prevents one failure from rolling
 * back the entire expiry batch.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentExpiryExecutor {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PayOS payOS;

    // Each payment expiry is committed independently — batch failure does not roll back prior items
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expireSinglePayment(PaymentTransaction tx) {
        // Re-check status: webhook may have confirmed payment concurrently
        var fresh =
                paymentTransactionRepository
                        .findById(tx.getId())
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "Payment not found: " + tx.getId()));
        if (fresh.getStatus() != PaymentStatus.PENDING) {
            log.info(
                    "Skipping expiry for orderCode={}: status already {}",
                    tx.getOrderCode(),
                    fresh.getStatus());
            return;
        }

        fresh.setStatus(PaymentStatus.EXPIRED);
        try {
            paymentTransactionRepository.save(fresh);
        } catch (ObjectOptimisticLockingFailureException e) {
            // Concurrent webhook confirmed the payment — skip
            log.info(
                    "Skipping expiry for orderCode={}: concurrently modified (version conflict)",
                    tx.getOrderCode());
            return;
        }

        try {
            payOS.paymentRequests().cancel(tx.getOrderCode(), "Payment link expired");
            log.info(
                    "Expired and cancelled PayOS link for orderCode={} company={}",
                    tx.getOrderCode(),
                    tx.getCompanyId());
        } catch (Exception e) {
            // Non-critical: link may already be expired/cancelled on PayOS side
            log.warn(
                    "Failed to cancel PayOS link for expired orderCode={}: {}",
                    tx.getOrderCode(),
                    e.getMessage());
        }
    }
}
