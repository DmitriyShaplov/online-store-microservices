package ru.shaplov.profile.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class AuthClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${clients.auth.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment("{profileId}").build().toUriString();
    }

    public void removeUser(Long profileId) {
        restTemplate.delete(baseUrl, profileId);
        log.info("user removed");
    }
}
