package com.vietrecruit.feature.notification.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.vietrecruit.common.config.kafka.KafkaTopicNames;
import com.vietrecruit.feature.notification.client.ResendEmailClient;
import com.vietrecruit.feature.notification.dto.EmailRequest;
import com.vietrecruit.feature.notification.service.EmailTemplateResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka consumer for email notifications. Consumes from {@link KafkaTopicNames#NOTIFICATION_EMAIL},
 * renders the email content, and delegates to {@link ResendEmailClient} for delivery.
 *
 * <p>Uses {@code @RetryableTopic} for Kafka-level retry with exponential backoff. Messages that
 * exhaust all retries are routed to the Dead Letter Topic (DLT) for manual inspection.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailTemplateResolver templateResolver;
    private final ResendEmailClient resendEmailClient;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = org.springframework.kafka.retrytopic.DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(
            topics = KafkaTopicNames.NOTIFICATION_EMAIL,
            groupId = "notification-email-group")
    public void consume(EmailRequest request) {
        log.info(
                "Consumed email notification from Kafka: to={}, subject={}",
                request.to(),
                request.subject());

        String renderedHtml = templateResolver.resolve(request);
        resendEmailClient.send(request, renderedHtml);
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, EmailRequest> record) {
        EmailRequest request = record.value();
        log.error(
                "Email notification moved to DLT after all retries exhausted: "
                        + "to={}, subject={}, topic={}, partition={}, offset={}",
                request.to(),
                request.subject(),
                record.topic(),
                record.partition(),
                record.offset());
    }
}
