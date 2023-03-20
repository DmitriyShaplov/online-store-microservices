package ru.shaplov.authservice.service;

import com.nimbusds.jwt.JWT;
import ru.shaplov.authservice.model.persistence.UserEntity;

public interface JwtService {

    JWT refreshExpiration(JWT jwt);

    JWT buildJwt(UserEntity userEntity);
}
