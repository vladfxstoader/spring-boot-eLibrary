package com.example.eLibrary.service;

import com.example.eLibrary.dto.UserDto;
import com.example.eLibrary.mapper.UserMapper;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.RoleRepository;
import com.example.eLibrary.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User resetPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User save(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus("PENDING");
        user.setPastRoles(new ArrayList<>());
        Role role = roleRepository.findByNameIgnoreCase("ROLE_USER");
//        Role role = roleRepository.findByNameIgnoreCase("ROLE_ADMIN"); // pentru a adauga admini
//        Role role = roleRepository.findByNameIgnoreCase("ROLE_LIBRARIAN"); // pentru a adauga bibliotecari
        user.setRoles(Arrays.asList(role));
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> userMapper.map(user))
                .collect(Collectors.toList());
    }

    public void changeUserStatus(Integer id, String status) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()) {
            throw new RuntimeException("There is no user with id " + id);
        }
        User user = optionalUser.get();
        user.setStatus(status);
        userRepository.save(user);
    }

    public void changeUserRole(String newRole, Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()) {
            throw new RuntimeException("There is no user with id " + id);
        }
        User user = optionalUser.get();
        Role role = roleRepository.findByNameIgnoreCase("ROLE_" + newRole);

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        List<Role> pastRoles = user.getPastRoles();
        Role currentRole = null;
        if (!user.getRoles().isEmpty()) {
            currentRole = user.getRoles().get(0);
            pastRoles.add(currentRole);
        }
        user.setRoles(new ArrayList<>(Arrays.asList(role)));
        user.setPastRoles(pastRoles);
        userRepository.save(user);
    }
}
