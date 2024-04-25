package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.UserDto;
import com.example.eLibrary.mapper.UserMapper;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.RoleRepository;
import com.example.eLibrary.repository.UserRepository;
import com.example.eLibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testResetPassword() {
        // Arrange
        String username = "test_user";
        String password = "new_password";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        userService.resetPassword(username, password);

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(user);
        log.info("Password reset for user '{}'", username);
    }

    @Test
    void testSave() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user");
        userDto.setPassword("password");

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        when(roleRepository.findByNameIgnoreCase("ROLE_USER")).thenReturn(role);

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("test_user");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        userService.save(userDto);

        // Assert
        verify(roleRepository, times(1)).findByNameIgnoreCase("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        log.info("User saved successfully: {}", userDto.getUsername());
    }

    @Test
    void testFindByUsername() {
        // Arrange
        String username = "test_user";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        User foundUser = userService.findByUsername(username);

        // Assert
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        log.info("User found by username '{}'", username);
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDto> foundUsers = userService.findAllUsers();

        // Assert
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        verify(userRepository, times(1)).findAll();
        log.info("All users retrieved successfully");
    }

    @Test
    void testChangeUserStatus() {
        // Arrange
        Integer userId = 1;
        String status = "ACCEPTED";
        User user = new User();
        user.setId(userId);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findById(userId)).thenReturn(optionalUser);

        // Act
        userService.changeUserStatus(userId, status);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        log.info("User status changed to '{}'", status);
    }

    @Test
    void testChangeUserRole() {
        // Arrange
        Integer userId = 1;
        String newRole = "ADMIN";
        User user = new User();
        user.setId(userId);
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_" + newRole);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByNameIgnoreCase("ROLE_" + newRole)).thenReturn(role);

        // Act
        userService.changeUserRole(newRole, userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findByNameIgnoreCase("ROLE_" + newRole);
        verify(userRepository, times(1)).save(user);
        log.info("User role changed to '{}'", newRole);
    }
}
