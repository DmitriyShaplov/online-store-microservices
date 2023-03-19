package ru.shaplov.account.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtPermissionChecker {

    public boolean check(Authentication authentication, Long profileId) {
        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            Long tokenProfileId = (Long) authenticationToken.getTokenAttributes().get("user_id");
            return tokenProfileId.equals(profileId);
        }
        return true;
    }
}
