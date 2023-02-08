package ru.shaplov.authservice.client.model;

import lombok.Data;

@Data
public class Profile {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
