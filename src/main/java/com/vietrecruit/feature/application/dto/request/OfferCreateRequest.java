package com.vietrecruit.feature.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
public class OfferCreateRequest {

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.01", message = "Base salary must be positive")
    private BigDecimal baseSalary;

    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String currency;

    private LocalDate startDate;

    private String note;

    @Size(max = 255, message = "Offer letter URL must not exceed 255 characters")
    private String offerLetterUrl;
}
