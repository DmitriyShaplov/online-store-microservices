package ru.shaplov.authservice.client.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String login;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
