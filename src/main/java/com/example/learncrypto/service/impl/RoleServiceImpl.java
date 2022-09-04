package com.example.learncrypto.service.impl;

import com.example.learncrypto.model.Role;
import com.example.learncrypto.repository.RoleRepository;
import com.example.learncrypto.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Transactional
    @Override
    public void deleteCurrentRolesForUser(String username) {

        List<Role> rolesOfUser = this.roleRepository.findAllByUserUsername(username);

        for (Role role : rolesOfUser) {
            this.roleRepository.deleteOneById(role.getId());
        }

    }

    @Override
    public Role getNewRole() {
        return new Role();
    }

}