package ru.shaplov.authservice.service;

import ru.shaplov.authservice.model.RegistrationRequest;
import ru.shaplov.authservice.model.User;

public interface RegistrationService {
    User register(RegistrationRequest request);
}
