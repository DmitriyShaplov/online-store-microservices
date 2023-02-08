package ru.shaplov.authservice.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jwt.JWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shaplov.authservice.service.JwtService;
import ru.shaplov.authservice.service.TokenStore;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenStoreImpl implements TokenStore {

    private final JwtService jwtService;
    private final Cache<String, JWT> sessionCache;

    public TokenStoreImpl(JwtService jwtService) {
        this.jwtService = jwtService;
        this.sessionCache = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .initialCapacity(10000)
                .build();
    }

    @Override
    public void storeToken(String sessionId, JWT jwt) {
        sessionCache.put(sessionId, jwt);
        log.info("CURRENT SESSIONS: {}", sessionCache.asMap().toString());
    }

    /**
     * Получаем сохраненный JWT по идентификатору сессии,
     * обновляем ключ, если он близок к инвалидации
     */
    @Override
    @SneakyThrows
    public JWT getToken(String sessionId) {
        log.info("CURRENT SESSIONS: {}", sessionCache.asMap().toString());
        JWT jwt = sessionCache.getIfPresent(sessionId);
        if (jwt != null && jwt.getJWTClaimsSet().getExpirationTime()
                .before(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))) {
            jwt = jwtService.refreshExpiration(jwt);
            storeToken(sessionId, jwt);
        }
        return jwt;
    }

    @Override
    public void removeToken(String sessionId) {
        sessionCache.invalidate(sessionId);
    }
}
