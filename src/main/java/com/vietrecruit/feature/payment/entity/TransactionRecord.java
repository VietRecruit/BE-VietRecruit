package com.vietrecruit.feature.payment.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_records")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_code", unique = true, nullable = false)
    private Long orderCode;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String reference;

    @Column(name = "transaction_date_time", nullable = false)
    private Instant transactionDateTime;

    @Column(name = "counter_account_bank_id", length = 50)
    private String counterAccountBankId;

    @Column(name = "counter_account_name")
    private String counterAccountName;

    @Column(name = "counter_account_number", length = 50)
    private String counterAccountNumber;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(name = "payment_link_id", length = 100)
    private String paymentLinkId;

    @Column(name = "payos_code", length = 10, nullable = false)
    private String payosCode;

    @Column(name = "payos_desc")
    private String payosDesc;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
