package dev.mstefanov.learncrypto.service.impl;

import dev.mstefanov.learncrypto.model.Role;
import dev.mstefanov.learncrypto.model.User;
import dev.mstefanov.learncrypto.model.service.UserServiceModel;
import dev.mstefanov.learncrypto.repository.UserRepository;
import dev.mstefanov.learncrypto.service.RoleService;
import dev.mstefanov.learncrypto.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            userRepository.save(newUser("demo", "demo123", "Demo", "User", "ROLE_USER"));
            userRepository.save(newUser("admin", "admin123", "Admin", "Account", "ROLE_ADMIN", "ROLE_USER"));
        }
    }

    private User newUser(String username, String rawPassword, String firstName, String lastName, String... roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Arrays.stream(roles).map(roleName -> {
            Role role = this.roleService.getNewRole();
            role.setName(roleName);
            role.setUser(user);
            return role;
        }).toList());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntityOpt = userRepository.findByUsername(username);

        return userEntityOpt.
                map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }
}
