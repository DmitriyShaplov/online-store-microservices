package ru.shaplov.profile.config;

import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.util.Base64;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**")
                .permitAll()
                .requestMatchers("/internal/**").permitAll()
                .anyRequest().hasAnyAuthority("SCOPE_profiles")
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "jwt-test.signing-key")
    public JwtDecoder jwtDecoderHmac(@Value("${jwt-test.signing-key}") String hmac) {
        log.warn("Using symmetric signing key for JWT.");
        return NimbusJwtDecoder
                .withSecretKey(buildSecretKey(hmac))
                .build();
    }

    private SecretKey buildSecretKey(String publicKey) {
        byte[] decode = Base64.getDecoder().decode(publicKey);
        return new SecretKeySpec(decode, "HS256");
    }

    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    @SneakyThrows
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        JWSAlgorithmFamilyJWSKeySelector<SecurityContext> jwsKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(
                new URL(properties.getJwt().getJwkSetUri()));

        DefaultJWTProcessor<SecurityContext> jwtProcessor =
                new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);

        return new NimbusJwtDecoder(jwtProcessor);
    }
}
