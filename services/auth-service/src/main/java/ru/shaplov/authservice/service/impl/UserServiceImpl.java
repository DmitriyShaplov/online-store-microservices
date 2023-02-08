package ru.shaplov.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shaplov.authservice.repository.UserRepository;
import ru.shaplov.authservice.service.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void removeByProfileId(Long profileId) {
        if (userRepository.removeByProfileId(profileId)) {
            log.info("user with profile_id {} removed", profileId);
        }
    }
}
