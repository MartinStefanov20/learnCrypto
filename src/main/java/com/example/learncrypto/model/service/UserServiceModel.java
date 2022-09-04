package com.example.learncrypto.model.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import static com.example.learncrypto.messages.ValidationErrorMessages.INCORRECT_FIRST_NAME_LENGTH;
import static com.example.learncrypto.messages.ValidationErrorMessages.INCORRECT_LAST_NAME_LENGTH;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserServiceModel {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
