package ru.shaplov.authservice.controller;

import com.nimbusds.jwt.JWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.shaplov.authservice.model.LoginRequest;
import ru.shaplov.authservice.service.AuthService;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping("/auth/**")
    public void enrichRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("INCOMING REQUEST, URL: {}", request.getRequestURI());
        HttpSession session = request.getSession(false);
        log.info("SESSION_ID: {}", session == null ? "NULL" : session.getId());
        JWT jwt = null;
        if (session != null) {
            jwt = authService.verifySession(session.getId());
        }
        if (session == null || jwt == null) {
            log.error("EXCEPTION, NO SESSION");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No session");
        }
        log.info("JWT: {}", jwt.serialize());
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.serialize());
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        authService.login(session.getId(), loginRequest);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            authService.logout(session.getId());
            session.invalidate();
        }
    }
}
