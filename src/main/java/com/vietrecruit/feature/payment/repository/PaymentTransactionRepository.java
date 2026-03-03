package com.vietrecruit.feature.payment.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.payment.entity.PaymentTransaction;
import com.vietrecruit.feature.payment.enums.PaymentStatus;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

    Optional<PaymentTransaction> findByOrderCode(Long orderCode);

    Optional<PaymentTransaction> findByCompanyIdAndStatus(UUID companyId, PaymentStatus status);

    @Query(
            """
			SELECT pt FROM PaymentTransaction pt
			WHERE pt.status = 'PENDING'
			AND pt.createdAt < :cutoff
			""")
    List<PaymentTransaction> findExpiredPending(@Param("cutoff") Instant cutoff);

    @Query(
            """
			SELECT pt FROM PaymentTransaction pt
			WHERE pt.status = 'PENDING'
			AND pt.createdAt < :cutoff
			AND pt.createdAt > :lowerBound
			""")
    List<PaymentTransaction> findStalePending(
            @Param("cutoff") Instant cutoff, @Param("lowerBound") Instant lowerBound);

    @Query(
            """
			SELECT pt FROM PaymentTransaction pt
			WHERE pt.status = com.vietrecruit.feature.payment.enums.PaymentStatus.PAID
			AND pt.companyId NOT IN (
				SELECT es.companyId FROM EmployerSubscription es
				WHERE es.status = com.vietrecruit.feature.subscription.enums.SubscriptionStatus.ACTIVE
			)
			""")
    List<PaymentTransaction> findPaidWithoutActiveSubscription();
}
