package com.vietrecruit.feature.notification.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.vietrecruit.feature.notification.dto.EmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resolves email content from an {@link EmailRequest}. Returns raw HTML directly or renders a
 * Thymeleaf template.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTemplateResolver {

    private final TemplateEngine templateEngine;

    /**
     * Resolves the final HTML content for the email.
     *
     * @param request the email request
     * @return rendered HTML string
     */
    public String resolve(EmailRequest request) {
        if (request.html() != null && !request.html().isBlank()) {
            log.debug("Using raw HTML content for email: subject={}", request.subject());
            return request.html();
        }

        log.debug(
                "Rendering Thymeleaf template: templateId={}, subject={}",
                request.templateId(),
                request.subject());

        Context context = new Context();
        Map<String, Object> variables =
                request.templateVariables() != null
                        ? request.templateVariables()
                        : Collections.emptyMap();
        context.setVariables(variables);

        return templateEngine.process("email/" + request.templateId(), context);
    }
}
