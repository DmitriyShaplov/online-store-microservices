package ru.shaplov.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shaplov.account.model.Account;
import ru.shaplov.account.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService profileService;

    public AccountController(AccountService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Account getUserProfile(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            Long tokenProfileId = (Long) authenticationToken.getTokenAttributes().get("user_id");
            profileService.find(tokenProfileId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/username/{username}")
    @PostAuthorize("@jwtPermissionChecker.check(authentication, returnObject.id)")
    public Account getByUsername(@PathVariable String username) {
        return profileService.findByUsername(username);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@jwtPermissionChecker.check(authentication, #id)")
    public Account find(@PathVariable Long id) {
        return profileService.find(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@jwtPermissionChecker.check(authentication, #id)")
    public void update(@RequestBody Account account, @PathVariable Long id) {
        account.setId(id);
        profileService.update(account);
    }
}
