package ru.shaplov.profile.service;

import ru.shaplov.profile.model.Profile;

import java.util.List;

public interface ProfileService {
    Profile create(Profile profile);
    Profile find(Long id);
    Profile findByUsername(String username);
    void delete(Long id);
    void update(Profile profile);

}
