package ru.shaplov.authservice.model;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @Size(min = 3)
    private String login;
    @Size(min = 6)
    private String password;

    private Profile profile;

    @Data
    public static class Profile {
        @Size(max = 256)
        private String username;
        private String firstName;
        private String lastName;
        @Pattern(regexp = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.\\p{L}{2,})$")
        private String email;
        @Pattern(regexp = "^\\+\\d{10}$")
        private String phone;
    }
}
