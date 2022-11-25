package ru.shaplov.userscrud.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.userscrud.model.User;
import ru.shaplov.userscrud.model.exception.ResponseCodeException;
import ru.shaplov.userscrud.model.persistence.UserEntity;
import ru.shaplov.userscrud.persistence.UserRepository;
import ru.shaplov.userscrud.service.UserService;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(User user) {
        if (user.getId() != null) {
            throw new IllegalStateException("Id should be null");
        }
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public User find(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(userEntity, User.class);
    }

    @Override
    public void delete(Long id) {
        if (1 != userRepository.deleteByIdNative(id)) {
            throw new ResponseCodeException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalStateException("Id should present");
        }
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseCodeException("User not found", HttpStatus.NOT_FOUND));
        modelMapper.map(user, userEntity);
    }

    @Override
    public List<Long> findAllIds() {
        return userRepository.findAllIds();
    }
}
