package ru.shaplov.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
        UserEntity userEntity = new UserEntity()
                .setLogin(request.getLogin())
                .setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity = userRepository.save(userEntity);

        Profile registeredProfile = null;
        try {
            registeredProfile = createProfile(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            userRepository.delete(userEntity);
            log.info("registration cancelled");
            throw e;
        }
        if (registeredProfile != null) {
            Assert.notNull(registeredProfile.getId(), "profile id is null!");
            userRepository.save(userEntity.setProfileId(registeredProfile.getId()));
            log.info("user updated with profile id");
        }
        return registeredProfile;
    }

    private Profile createProfile(RegistrationRequest request) {
        Profile profile = modelMapper.map(request.getProfile(), Profile.class);
        return profileClient.register(profile);
    }
}
