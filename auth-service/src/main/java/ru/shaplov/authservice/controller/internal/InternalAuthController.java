package ru.shaplov.authservice.controller.internal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.authservice.service.UserService;

@RestController
public class InternalAuthController {

    private final UserService userService;

    public InternalAuthController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/internal/{profileId}")
    public void removeUser(@PathVariable Long profileId) {
        userService.removeByProfileId(profileId);
    }
}
