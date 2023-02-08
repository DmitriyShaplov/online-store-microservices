package ru.shaplov.authservice.service;

import com.nimbusds.jwt.JWT;

public interface TokenStore {

    void storeToken(String sessionId, JWT jwt);

    JWT getToken(String sessionId);

    void removeToken(String sessionId);
}
