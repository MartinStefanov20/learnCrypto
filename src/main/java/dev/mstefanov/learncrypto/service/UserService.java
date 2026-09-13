package dev.mstefanov.learncrypto.service;

import dev.mstefanov.learncrypto.model.User;
import dev.mstefanov.learncrypto.model.service.UserServiceModel;

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
