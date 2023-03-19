package ru.shaplov.authservice.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @Size(min = 3)
    @NotNull
    private String login;
    @Size(min = 6)
    @NotNull
    private String password;

    @Size(max = 256)
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Pattern(regexp = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.\\p{L}{2,})$")
    @NotNull
    private String email;
    @Pattern(regexp = "^\\+\\d{10}$")
    @NotNull
    private String phone;
}
