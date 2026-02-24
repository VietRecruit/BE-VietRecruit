package com.vietrecruit.feature.notification.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vietrecruit.common.config.kafka.KafkaTopicNames;
import com.vietrecruit.feature.notification.dto.EmailRequest;
import com.vietrecruit.feature.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(EmailRequest request) {
        log.info(
                "Publishing email notification to Kafka: to={}, subject={}",
                request.to(),
                request.subject());

        kafkaTemplate
                .send(KafkaTopicNames.NOTIFICATION_EMAIL, request)
                .whenComplete(
                        (result, ex) -> {
                            if (ex != null) {
                                log.error(
                                        "Failed to publish email notification to Kafka: to={}, subject={}",
                                        request.to(),
                                        request.subject(),
                                        ex);
                            } else {
                                log.debug(
                                        "Email notification published to Kafka: topic={}, offset={}",
                                        result.getRecordMetadata().topic(),
                                        result.getRecordMetadata().offset());
                            }
                        });
    }
}
