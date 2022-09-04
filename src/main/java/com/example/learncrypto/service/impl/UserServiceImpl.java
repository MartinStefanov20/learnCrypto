package com.example.learncrypto.service.impl;

import com.example.learncrypto.model.Result;
import com.example.learncrypto.model.Role;
import com.example.learncrypto.model.User;
import com.example.learncrypto.model.service.UserServiceModel;
import com.example.learncrypto.repository.UserRepository;
import com.example.learncrypto.service.RoleService;
import com.example.learncrypto.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    private UserDetails map(User userEntity) {

        Stream<String> s = Stream.of("User");


        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.
                        getRoles().
                        stream().
                        map(this::map).
                        collect(Collectors.toList())
        );
    }

    private GrantedAuthority map(Role role) {
        return new SimpleGrantedAuthority(role.getName());
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.passwordEncoder.encode(userServiceModel.getPassword()));

        this.userRepository.save(user);
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return this.userRepository.findByUsername(username).orElse(null) != null;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public String getUserFullName(Long patientId) {
        User user = this.userRepository.findOneById(patientId);

        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public User getUserById(Long patientId) {
        return this.userRepository.findOneById(patientId);
    }

    @Override
    public List<String> getAllUsernames() {
        return this.userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
    }

    @Override
    public void initUsers() {
        if (this.userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("martin.stefanov");
            user1.setPassword(passwordEncoder.encode("martin"));
            user1.setFirstName("Martin");
            user1.setLastName("Stefanov");

            Role roleUser = this.roleService.getNewRole();
            roleUser.setName("ROLE_USER");
            roleUser.setUser(user1);

            user1.setRoles(List.of(roleUser));


            userRepository.save(user1);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntityOpt = userRepository.findByUsername(username);

        return userEntityOpt.
                map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }
}
