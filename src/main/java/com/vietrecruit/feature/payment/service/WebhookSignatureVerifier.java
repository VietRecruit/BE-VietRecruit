package com.vietrecruit.feature.payment.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietrecruit.common.config.PayOSConfig;
import com.vietrecruit.feature.payment.exception.WebhookVerificationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Verifies PayOS webhook signatures using HMAC-SHA256 with timing-safe comparison.
 *
 * <p>Algorithm (mirrors PayOS SDK v2.0.1 CryptoProviderImpl):
 *
 * <ol>
 *   <li>Convert the {@code data} object to a flat {@code Map<String, Object>} via Jackson.
 *   <li>Sort the map alphabetically by key (TreeMap).
 *   <li>Build a string: {@code key1=value1&key2=value2&...} (null → empty string).
 *   <li>Compute HMAC-SHA256 using the checksum key.
 *   <li>Compare with the provided signature using {@link MessageDigest#isEqual} (constant-time).
 * </ol>
 */
@Slf4j
@Component
public class WebhookSignatureVerifier {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final byte[] checksumKeyBytes;

    public WebhookSignatureVerifier(PayOSConfig payOSConfig) {
        this.checksumKeyBytes = payOSConfig.getChecksumKey().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Verifies that the provided signature matches the HMAC-SHA256 of the data object.
     *
     * @param data the webhook data object (will be serialized to sorted key=value pairs)
     * @param signature the signature string from the webhook payload
     * @throws WebhookVerificationException if the signature is missing, empty, or does not match
     */
    public void verify(Object data, String signature) {
        if (signature == null || signature.isEmpty()) {
            throw new WebhookVerificationException("Webhook signature is missing or empty");
        }
        if (data == null) {
            throw new WebhookVerificationException("Webhook data is null");
        }

        String computed = computeSignature(data);

        if (log.isDebugEnabled()) {
            log.debug(
                    "Webhook signature verification — computed={}, received={}",
                    computed,
                    signature);
        }

        byte[] computedBytes = computed.getBytes(StandardCharsets.UTF_8);
        byte[] receivedBytes = signature.getBytes(StandardCharsets.UTF_8);

        if (!MessageDigest.isEqual(computedBytes, receivedBytes)) {
            throw new WebhookVerificationException("Webhook signature mismatch");
        }
    }

    private String computeSignature(Object data) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> dataMap = OBJECT_MAPPER.convertValue(data, Map.class);
            TreeMap<String, Object> sorted = new TreeMap<>(dataMap);

            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                if (!first) {
                    sb.append('&');
                }
                first = false;
                sb.append(entry.getKey());
                sb.append('=');
                Object value = entry.getValue();
                if (value != null) {
                    sb.append(value);
                }
            }

            String dataString = sb.toString();
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(checksumKeyBytes, HMAC_SHA256));
            byte[] hash = mac.doFinal(dataString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new WebhookVerificationException("HMAC computation failed", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
