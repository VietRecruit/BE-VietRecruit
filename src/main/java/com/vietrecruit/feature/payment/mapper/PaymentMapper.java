package com.vietrecruit.feature.payment.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vietrecruit.feature.payment.dto.response.CheckoutResponse;
import com.vietrecruit.feature.payment.dto.response.PaymentStatusResponse;
import com.vietrecruit.feature.payment.dto.response.TransactionHistoryResponse;
import com.vietrecruit.feature.payment.entity.PaymentTransaction;
import com.vietrecruit.feature.payment.entity.TransactionRecord;

import vn.payos.model.webhooks.WebhookData;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    DateTimeFormatter PAYOS_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "checkoutUrl", source = "checkoutUrl")
    @Mapping(target = "orderCode", source = "orderCode")
    CheckoutResponse toCheckoutResponse(PaymentTransaction transaction);

    @Mapping(target = "orderCode", source = "orderCode")
    @Mapping(target = "status", expression = "java(transaction.getStatus().name())")
    @Mapping(target = "planName", source = "plan.name")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "createdAt", source = "createdAt")
    PaymentStatusResponse toPaymentStatusResponse(PaymentTransaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyId", source = "companyId")
    @Mapping(target = "orderCode", source = "data.orderCode")
    @Mapping(target = "accountNumber", source = "data.accountNumber")
    @Mapping(target = "amount", source = "data.amount")
    @Mapping(target = "description", source = "data.description")
    @Mapping(target = "reference", source = "data.reference")
    @Mapping(
            target = "transactionDateTime",
            source = "data.transactionDateTime",
            qualifiedByName = "parsePayOSDateTime")
    @Mapping(target = "counterAccountBankId", source = "data.counterAccountBankId")
    @Mapping(target = "counterAccountName", source = "data.counterAccountName")
    @Mapping(target = "counterAccountNumber", source = "data.counterAccountNumber")
    @Mapping(target = "currency", source = "data.currency")
    @Mapping(target = "paymentLinkId", source = "data.paymentLinkId")
    @Mapping(target = "payosCode", source = "data.code")
    @Mapping(target = "payosDesc", source = "data.desc")
    @Mapping(target = "createdAt", ignore = true)
    TransactionRecord toTransactionRecord(WebhookData data, UUID companyId);

    @Mapping(target = "status", source = "payosCode", qualifiedByName = "mapPayOSStatus")
    TransactionHistoryResponse toTransactionHistoryResponse(TransactionRecord record);

    @Named("parsePayOSDateTime")
    default Instant parsePayOSDateTime(String dateTime) {
        if (dateTime == null || dateTime.isBlank()) {
            return Instant.now();
        }
        return LocalDateTime.parse(dateTime, PAYOS_DATETIME_FORMAT)
                .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                .toInstant();
    }

    @Named("mapPayOSStatus")
    default String mapPayOSStatus(String payosCode) {
        if ("00".equals(payosCode)) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
