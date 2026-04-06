package com.vietrecruit.feature.payment.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vietrecruit.feature.payment.enums.PaymentStatus;
import com.vietrecruit.feature.payment.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled task that expires pending payment transactions older than 30 minutes. Runs every 15
 * minutes. Also cancels the corresponding PayOS payment link.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentExpiryTask {

    private static final int EXPIRY_MINUTES = 30;

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentExpiryExecutor expiryExecutor;

    @Scheduled(fixedRate = 900_000) // 15 minutes
    public void expirePendingPayments() {
        var cutoff = Instant.now().minus(EXPIRY_MINUTES, ChronoUnit.MINUTES);
        var expired =
                paymentTransactionRepository.findExpiredPending(cutoff, PaymentStatus.PENDING);

        if (expired.isEmpty()) {
            log.debug("No expired pending payments found");
            return;
        }

        log.info("Expiring {} pending payment(s)", expired.size());

        for (var tx : expired) {
            try {
                expiryExecutor.expireSinglePayment(tx);
            } catch (Exception e) {
                log.error(
                        "Failed to expire payment orderCode={}: {}",
                        tx.getOrderCode(),
                        e.getMessage());
            }
        }
    }
}
