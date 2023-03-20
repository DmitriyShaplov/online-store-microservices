package ru.shaplov.authservice.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.shaplov.authservice.model.persistence.UserEntity;
import ru.shaplov.authservice.service.JwtService;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final JWSSigner jwsSigner;

    public JwtServiceImpl(ECKey ecKey) throws JOSEException {
        this.jwsSigner = new ECDSASigner(ecKey);
    }

    @SneakyThrows
    @Override
    public JWT refreshExpiration(JWT jwt) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder(jwt.getJWTClaimsSet())
                .expirationTime(getExpDate())
                .build();
        SignedJWT signedJWT = new SignedJWT(((SignedJWT) jwt).getHeader(), jwtClaimsSet);
        signedJWT.sign(jwsSigner);
        return signedJWT;
    }

    @SneakyThrows
    private JWT buildJwt(JWTClaimsSet claimsSet) {
        // Create a SignedJWT with the JWTClaimsSet and the OctetKeyPair
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.ES256).build(), claimsSet);
        signedJWT.sign(jwsSigner);
        return signedJWT;
    }

    @Override
    public JWT buildJwt(UserEntity userEntity) {

        // Create a JWTClaimsSet with the desired claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userEntity.getLogin())
                .claim("user_id", userEntity.getId())
                .claim("username", userEntity.getUsername())
                .claim("firstName", userEntity.getFirstName())
                .claim("lastName", userEntity.getLastName())
                .claim("email", userEntity.getEmail())
                .claim("phone", userEntity.getPhone())
                .claim("scope", "user")
                .expirationTime(new Date(System.currentTimeMillis() + 1800 * 1000))
                .build();

        return buildJwt(claimsSet);
    }

    private Date getExpDate() {
        return new Date(System.currentTimeMillis() + 1800 * 1000);
    }
}
