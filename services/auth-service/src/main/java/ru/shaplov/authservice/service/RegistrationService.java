package ru.shaplov.authservice.service;

import ru.shaplov.authservice.client.model.User;
import ru.shaplov.authservice.model.RegistrationRequest;

public interface RegistrationService {
    User register(RegistrationRequest request);
}
