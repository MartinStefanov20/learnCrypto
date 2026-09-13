package dev.mstefanov.learncrypto.service;


import dev.mstefanov.learncrypto.model.Role;
import dev.mstefanov.learncrypto.model.Role;

public interface RoleService {

    void deleteCurrentRolesForUser(String username);

    Role getNewRole();

}
