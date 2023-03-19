package ru.shaplov.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shaplov.authservice.model.RegistrationRequest;
import ru.shaplov.authservice.model.persistence.UserEntity;
import ru.shaplov.authservice.repository.UserRepository;
import ru.shaplov.authservice.service.RegistrationService;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserRepository userRepository,
                                   ModelMapper modelMapper,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegistrationRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntity);
        log.info("User registered {}", userEntity);
        return modelMapper.map(userEntity, User.class);
    }
}
