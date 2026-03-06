package com.vietrecruit.feature.payment.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.payment.entity.TransactionRecord;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, UUID> {

    Page<TransactionRecord> findByCompanyIdOrderByCreatedAtDesc(UUID companyId, Pageable pageable);

    Page<TransactionRecord> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByOrderCode(Long orderCode);
}
