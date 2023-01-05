package ru.shaplov.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shaplov.profile.model.Profile;
import ru.shaplov.profile.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Profile getUserProfile(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            Long tokenProfileId = (Long) authenticationToken.getTokenAttributes().get("profile_id");
            profileService.find(tokenProfileId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/username/{username}")
    @PostAuthorize("@jwtPermissionChecker.check(authentication, returnObject.id)")
    public Profile getByUsername(@PathVariable String username) {
        return profileService.findByUsername(username);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@jwtPermissionChecker.check(authentication, #id)")
    public Profile find(@PathVariable Long id) {
        return profileService.find(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@jwtPermissionChecker.check(authentication, #id)")
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@jwtPermissionChecker.check(authentication, #id)")
    public void update(@RequestBody Profile profile, @PathVariable Long id) {
        profile.setId(id);
        profileService.update(profile);
    }
}
