package com.vietrecruit.feature.payment.exception;

/**
 * Thrown when the webhook signature verification fails. Indicates the request is either forged or
 * tampered with.
 */
public class WebhookVerificationException extends RuntimeException {

    public WebhookVerificationException(String message) {
        super(message);
    }

    public WebhookVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
