package com.vietrecruit.feature.application.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.vietrecruit.feature.application.enums.OfferStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {
    private UUID id;
    private UUID applicationId;
    private String offerLetterUrl;
    private BigDecimal baseSalary;
    private String currency;
    private LocalDate startDate;
    private String note;
    private OfferStatus status;
    private Instant createdAt;
}
