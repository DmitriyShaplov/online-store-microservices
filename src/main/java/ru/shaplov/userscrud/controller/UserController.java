package ru.shaplov.userscrud.controller;

import org.springframework.web.bind.annotation.*;
import ru.shaplov.userscrud.model.User;
import ru.shaplov.userscrud.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void create(@RequestBody User user) {
        userService.create(user);
    }

    @GetMapping("/{id}")
    public User find(@PathVariable Long id) {
        return userService.find(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody User user) {
        userService.update(user);
    }

    @GetMapping("/ids")
    public List<Long> findAllIds() {
        return userService.findAllIds();
    }
}
