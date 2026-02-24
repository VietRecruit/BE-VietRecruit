package com.vietrecruit.common.security.oauth2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("github".equals(registrationId) && oAuth2User.getAttribute("email") == null) {
            String email = fetchGitHubPrimaryEmail(userRequest.getAccessToken().getTokenValue());
            if (email != null) {
                Map<String, Object> attributes =
                        new java.util.HashMap<>(oAuth2User.getAttributes());
                attributes.put("email", email);

                return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                        oAuth2User.getAuthorities(),
                        Collections.unmodifiableMap(attributes),
                        userRequest
                                .getClientRegistration()
                                .getProviderDetails()
                                .getUserInfoEndpoint()
                                .getUserNameAttributeName());
            } else {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("email_not_found"),
                        "Email not available from GitHub. Please make your email public or add a verified email.");
            }
        }

        return oAuth2User;
    }

    private String fetchGitHubPrimaryEmail(String accessToken) {
        try {
            RestClient restClient = RestClient.create();
            List<Map<String, Object>> emails =
                    restClient
                            .get()
                            .uri("https://api.github.com/user/emails")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {});

            if (emails == null) {
                return null;
            }

            return emails.stream()
                    .filter(
                            e ->
                                    Boolean.TRUE.equals(e.get("primary"))
                                            && Boolean.TRUE.equals(e.get("verified")))
                    .map(e -> (String) e.get("email"))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Failed to fetch GitHub emails: {}", e.getMessage());
            return null;
        }
    }
}
