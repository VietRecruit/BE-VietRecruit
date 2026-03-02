package com.vietrecruit.feature.payment.service;

import java.util.UUID;

import com.vietrecruit.feature.payment.dto.response.CheckoutResponse;
import com.vietrecruit.feature.payment.dto.response.PaymentStatusResponse;
import com.vietrecruit.feature.subscription.entity.BillingCycle;

public interface PaymentService {

    CheckoutResponse initiateCheckout(UUID companyId, UUID planId, BillingCycle cycle);

    void handleWebhook(Object webhookBody);

    PaymentStatusResponse getPaymentStatus(Long orderCode, UUID companyId);
}
