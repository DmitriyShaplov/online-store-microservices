package ru.shaplov.common.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.shaplov.common.client.AuthClient;

@Configuration
public class ClientsConfiguration {

    @Bean
    @ConditionalOnProperty(name = "clients.auth.url")
    AuthClient authClient(RestTemplate restTemplate,
                          @Value("${clients.auth.url}") String baseUrl) {
        return new AuthClient(restTemplate, baseUrl);
    }
}
