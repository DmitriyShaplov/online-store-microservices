package ru.shaplov.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shaplov.authservice.client.ProfileClient;
import ru.shaplov.authservice.client.model.Profile;
import ru.shaplov.authservice.model.RegistrationRequest;
import ru.shaplov.authservice.model.persistence.UserEntity;
import ru.shaplov.authservice.repository.UserRepository;
import ru.shaplov.authservice.service.RegistrationService;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final ProfileClient profileClient;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserRepository userRepository,
                                   ProfileClient profileClient,
                                   ModelMapper modelMapper,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileClient = profileClient;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Profile register(RegistrationRequest request) {
        Profile registeredProfile = createProfile(request);
        UserEntity userEntity = new UserEntity()
                .setLogin(request.getLogin())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setProfileId(registeredProfile.getId());
        userRepository.save(userEntity);
        log.info("User registered {}", registeredProfile);
        return registeredProfile;
    }

    private Profile createProfile(RegistrationRequest request) {
        Profile profile = modelMapper.map(request.getProfile(), Profile.class);
        return profileClient.register(profile);
    }
}
