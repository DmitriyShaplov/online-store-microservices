package ru.shaplov.authservice.service;

import ru.shaplov.authservice.client.model.Profile;
import ru.shaplov.authservice.model.RegistrationRequest;

public interface RegistrationService {
    Profile register(RegistrationRequest request);
}
