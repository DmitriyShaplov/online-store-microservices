package ru.shaplov.authservice.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtService {

    JWT refreshExpiration(JWT jwt);

    JWT buildJwt(JWTClaimsSet claimsSet);
}
