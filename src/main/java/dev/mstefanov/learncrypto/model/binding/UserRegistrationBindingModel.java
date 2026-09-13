package dev.mstefanov.learncrypto.model.binding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


import static dev.mstefanov.learncrypto.messages.ValidationErrorMessages.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationBindingModel {


    @Size(min = 4, max = 30, message = INCORRECT_USERNAME_LENGTH)
    private String username;
    @Size(min = 4, max = 16, message = INCORRECT_PASSWORD_LENGTH)
    private String password;
    @Size(min = 2, max = 25, message = INCORRECT_FIRST_NAME_LENGTH)
    private String confirmPassword;
    @Size(min = 2, max = 25, message = INCORRECT_FIRST_NAME_LENGTH)
    private String firstName;
    @Size(min = 2, max = 25, message = INCORRECT_LAST_NAME_LENGTH)
    private String lastName;

}