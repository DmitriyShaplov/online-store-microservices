package ru.shaplov.common.starter;

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
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.util.Base64;

@Configuration
@Slf4j
public class SecurityStarter {
    @Bean
    @ConditionalOnProperty(name = "jwt-test.signing-key")
    @Primary
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
    @Primary
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        JWSAlgorithmFamilyJWSKeySelector<SecurityContext> jwsKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(
                new URL(properties.getJwt().getJwkSetUri()));

        DefaultJWTProcessor<SecurityContext> jwtProcessor =
                new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);

        return new NimbusJwtDecoder(jwtProcessor);
    }
}
