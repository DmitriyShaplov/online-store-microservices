package ru.shaplov.authservice.service.impl;

import com.nimbusds.jwt.JWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.shaplov.authservice.model.LoginRequest;
import ru.shaplov.authservice.model.persistence.UserEntity;
import ru.shaplov.authservice.repository.UserRepository;
import ru.shaplov.authservice.service.AuthService;
import ru.shaplov.authservice.service.JwtService;
import ru.shaplov.authservice.service.TokenStore;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenStore tokenStore,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.jwtService = jwtService;
    }

    @Override
    public void login(String sessionId, LoginRequest request) {
        UserEntity userEntity = userRepository.findByLogin(request.getLogin()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user doesn't exist")
        );
        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password doesn't match");
        }

        JWT jwt = jwtService.buildJwt(userEntity);

        tokenStore.storeToken(sessionId, jwt);
    }

    @Override
    @SneakyThrows
    public JWT verifySession(String sessionId) {
        JWT jwt = tokenStore.getToken(sessionId);
        if (jwt == null || jwt.getJWTClaimsSet().getExpirationTime().before(Date.from(Instant.now()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        return jwt;
    }

    @Override
    public void logout(String sessionId) {
        tokenStore.removeToken(sessionId);
        log.info("session {} invalidated", sessionId);
    }
}
