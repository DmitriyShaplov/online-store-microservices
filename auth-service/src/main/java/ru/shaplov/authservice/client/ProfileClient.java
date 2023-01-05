package ru.shaplov.authservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.shaplov.authservice.client.model.Profile;

@Component
public class ProfileClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProfileClient(RestTemplate restTemplate,
                         @Value("${clients.profile.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Profile register(Profile profile) {
        String uriString = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/internal/profile").toUriString();
        return restTemplate.postForObject(uriString, profile, Profile.class);
    }
}
