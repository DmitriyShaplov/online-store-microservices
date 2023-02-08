package ru.shaplov.profile.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.client.AuthClient;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplov.profile.model.Profile;
import ru.shaplov.profile.model.persistence.ProfileEntity;
import ru.shaplov.profile.persistence.ProfileRepository;
import ru.shaplov.profile.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final AuthClient authClient;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ModelMapper modelMapper,
                              AuthClient authClient) {
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
        this.authClient = authClient;
    }

    @Override
    @Transactional
    public Profile create(Profile profile) {
        if (profile.getId() != null) {
            throw new IllegalStateException("Id should be null");
        }
        ProfileEntity profileEntity = modelMapper.map(profile, ProfileEntity.class);
        profileEntity = profileRepository.save(profileEntity);
        return find(profileEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Profile find(Long id) {
        ProfileEntity profileEntity = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(profileEntity, Profile.class);
    }

    @Override
    public Profile findByUsername(String username) {
        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        return modelMapper.map(profileEntity, Profile.class);
    }

    @Override
    public void delete(Long id) {
        authClient.removeUser(id);
        if (1 != profileRepository.deleteByIdNative(id)) {
            throw new ResponseCodeException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void update(Profile profile) {
        if (profile.getId() == null) {
            throw new IllegalStateException("Id should present");
        }
        ProfileEntity profileEntity = profileRepository.findById(profile.getId())
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        ProfileEntity entityToSave = modelMapper.map(profile, ProfileEntity.class);
        entityToSave.setId(profileEntity.getId());
        profileRepository.save(entityToSave);
    }
}
