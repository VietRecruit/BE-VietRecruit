package com.vietrecruit.feature.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.vietrecruit.common.config.PayOSConfig;
import com.vietrecruit.feature.payment.exception.WebhookVerificationException;

class WebhookSignatureVerifierTest {

    private static final String TEST_CHECKSUM_KEY = "test-checksum-key-for-unit-tests";

    private WebhookSignatureVerifier verifier;

    @BeforeEach
    void setUp() {
        PayOSConfig config = new PayOSConfig();
        config.setChecksumKey(TEST_CHECKSUM_KEY);
        verifier = new WebhookSignatureVerifier(config);
    }

    @Test
    @DisplayName("Valid signature — should not throw")
    void verify_validSignature_noException() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("orderCode", 123456L);
        data.put("amount", 50000L);
        data.put("description", "Test payment");

        String signature = computeExpectedSignature(data);

        assertDoesNotThrow(() -> verifier.verify(data, signature));
    }

    @Test
    @DisplayName("Invalid signature — should throw WebhookVerificationException")
    void verify_invalidSignature_throws() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("orderCode", 123456L);
        data.put("amount", 50000L);

        var ex =
                assertThrows(
                        WebhookVerificationException.class,
                        () -> verifier.verify(data, "deadbeef"));
        assertTrue(ex.getMessage().contains("mismatch"));
    }

    @Test
    @DisplayName("Null signature — should throw WebhookVerificationException")
    void verify_nullSignature_throws() {
        Map<String, Object> data = Map.of("orderCode", 123456L);

        var ex =
                assertThrows(WebhookVerificationException.class, () -> verifier.verify(data, null));
        assertTrue(ex.getMessage().contains("missing"));
    }

    @Test
    @DisplayName("Empty signature — should throw WebhookVerificationException")
    void verify_emptySignature_throws() {
        Map<String, Object> data = Map.of("orderCode", 123456L);

        var ex = assertThrows(WebhookVerificationException.class, () -> verifier.verify(data, ""));
        assertTrue(ex.getMessage().contains("missing"));
    }

    @Test
    @DisplayName("Null data — should throw WebhookVerificationException")
    void verify_nullData_throws() {
        var ex =
                assertThrows(
                        WebhookVerificationException.class,
                        () -> verifier.verify(null, "some-signature"));
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    @DisplayName("Data with null values — null rendered as empty string in signature")
    void verify_dataWithNullValues_handledCorrectly() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("amount", 50000L);
        data.put("description", null);
        data.put("orderCode", 123456L);

        String signature = computeExpectedSignature(data);

        assertDoesNotThrow(() -> verifier.verify(data, signature));
    }

    /**
     * Computes the expected HMAC-SHA256 signature using the same algorithm as
     * WebhookSignatureVerifier: sorted key=value pairs joined by &.
     */
    private String computeExpectedSignature(Map<String, Object> data) {
        TreeMap<String, Object> sorted = new TreeMap<>(data);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : sorted.entrySet()) {
            if (!first) sb.append('&');
            first = false;
            sb.append(entry.getKey()).append('=');
            if (entry.getValue() != null) {
                sb.append(entry.getValue());
            }
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(
                    new SecretKeySpec(
                            TEST_CHECKSUM_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(sb.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
