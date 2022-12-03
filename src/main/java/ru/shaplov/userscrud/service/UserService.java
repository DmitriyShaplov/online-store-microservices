package ru.shaplov.userscrud.service;

import ru.shaplov.userscrud.model.User;

import java.util.List;

public interface UserService {
    void create(User user);
    User find(Long id);
    void delete(Long id);
    void update(User user);

    List<User> findAll();
}
