package ru.shaplov.authservice.service;

import com.nimbusds.jwt.JWT;
import ru.shaplov.authservice.model.LoginRequest;

public interface AuthService {

    void login(String sessionId, LoginRequest request);

    JWT verifySession(String sessionId);

    void logout(String sessionId);
}
