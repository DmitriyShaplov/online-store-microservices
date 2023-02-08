package ru.shaplov.profile.controller.internal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.profile.model.Profile;
import ru.shaplov.profile.service.ProfileService;

@RestController
@RequestMapping("/internal/profile")
public class RegistrationController {

    private final ProfileService profileService;

    public RegistrationController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public Profile registration(@RequestBody Profile profile) {
        return profileService.create(profile);
    }
}
