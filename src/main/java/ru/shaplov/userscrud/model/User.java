package ru.shaplov.userscrud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
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
