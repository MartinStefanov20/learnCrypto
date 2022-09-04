package com.example.learncrypto.service;

import com.example.learncrypto.model.User;
import com.example.learncrypto.model.service.UserServiceModel;

import java.util.List;

public interface UserService {

    void registerUser(UserServiceModel userServiceModel);

    boolean checkIfUsernameExists(String username);

    User getUserByUsername(String username);

    String getUserFullName(Long patientId);

    User getUserById(Long patientId);

    List<String> getAllUsernames();

    void initUsers();
}
