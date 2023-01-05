package ru.shaplov.profile;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.gen.JWKGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@ActiveProfiles(profiles = {"test"})
class KuberUsersCrudApplicationTests {

    @Bean
    public JwtDecoder jwtDecoderHmac() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("SHA-256").generateKey();
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .build();
    }

    @Test
    void contextLoads() {
    }

}
