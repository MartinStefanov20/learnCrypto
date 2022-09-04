package com.example.learncrypto.service;


import com.example.learncrypto.model.Role;
import com.example.learncrypto.model.Role;

public interface RoleService {

    void deleteCurrentRolesForUser(String username);

    Role getNewRole();

}
