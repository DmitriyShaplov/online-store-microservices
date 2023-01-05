package ru.shaplov.authservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.authservice.client.model.Profile;
import ru.shaplov.authservice.model.RegistrationRequest;
import ru.shaplov.authservice.service.RegistrationService;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public Profile registerUser(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
}
