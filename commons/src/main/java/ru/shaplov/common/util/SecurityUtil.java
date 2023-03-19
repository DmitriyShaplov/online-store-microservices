package ru.shaplov.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@UtilityClass
public class SecurityUtil {

    public Long getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() instanceof JwtAuthenticationToken token) {
            return token.getToken().getClaim("user_id");
        }
        throw new IllegalStateException("user is not authenticated");
    }
}
