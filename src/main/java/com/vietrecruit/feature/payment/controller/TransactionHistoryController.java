package com.vietrecruit.feature.payment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vietrecruit.common.ApiConstants;
import com.vietrecruit.common.base.BaseController;
import com.vietrecruit.common.enums.ApiSuccessCode;
import com.vietrecruit.common.response.ApiResponse;
import com.vietrecruit.feature.payment.dto.response.TransactionHistoryResponse;
import com.vietrecruit.feature.payment.service.TransactionHistoryService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Payment.ROOT)
@Tag(
        name = "Transaction History",
        description = "Endpoints for viewing payment transaction history")
public class TransactionHistoryController extends BaseController {

    private final TransactionHistoryService transactionHistoryService;

    @Operation(
            summary = "Get Transaction History",
            description =
                    "Retrieves paginated transaction history for the authenticated user's company")
    @RateLimiter(name = "mediumTraffic", fallbackMethod = "rateLimit")
    @GetMapping(ApiConstants.Payment.TRANSACTIONS)
    public ResponseEntity<ApiResponse<Page<TransactionHistoryResponse>>> getTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        var companyId = resolveCompanyId();
        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiSuccessCode.TRANSACTION_HISTORY_FETCH_SUCCESS,
                        transactionHistoryService.getCompanyTransactions(companyId, pageable)));
    }
}
