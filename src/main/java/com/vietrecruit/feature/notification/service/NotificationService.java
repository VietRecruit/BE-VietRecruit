package com.vietrecruit.feature.notification.service;

import com.vietrecruit.feature.notification.dto.EmailRequest;

/** Public interface for sending email notifications. Callers depend only on this contract. */
public interface NotificationService {

    /**
     * Sends an email notification asynchronously via the internal messaging infrastructure.
     *
     * @param request the email request payload
     */
    void send(EmailRequest request);
}
