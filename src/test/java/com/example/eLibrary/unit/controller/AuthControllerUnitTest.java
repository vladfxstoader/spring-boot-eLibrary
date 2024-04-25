package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.AuthController;
import com.example.eLibrary.dto.RoleDto;
import com.example.eLibrary.dto.UserDto;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.RoleService;
import com.example.eLibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testAccessDeniedPage() {
        // Act
        String viewName = authController.accessDeniedPage();

        // Assert
        assertEquals("access-denied", viewName);
        log.info("Access Denied Page Viewed");
    }

    @Test
    void testHome() {
        // Arrange
        Model model = mock(Model.class);
        User existingUser = new User();
        existingUser.setUsername("testUser");
        Role role = new Role();
        role.setName("ROLE_USER");
        existingUser.setRoles(List.of(role));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Define the username for the mocked Authentication object
        when(authentication.getName()).thenReturn("testUser");

        when(userService.findByUsername("testUser")).thenReturn(existingUser);

        // Act
        String viewName = authController.home(model);

        // Assert
        assertEquals("index", viewName);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("role", "ROLE_USER");
        verify(userService, times(1)).findByUsername("testUser");
        log.info("Home Page Viewed");
    }

    @Test
    void testShowRegistrationForm() {
        // Arrange
        Model model = mock(Model.class);

        // Act
        String viewName = authController.showRegistrationForm(model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute("user", new UserDto());
        log.info("Registration Form Displayed");
    }

    @Test
    void testRegistration() {
        // Arrange
        Model model = mock(Model.class);
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        BindingResult bindingResult = mock(BindingResult.class);

        // Act
        String viewName = authController.registration(userDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/register?success", viewName);
        verify(userService).save(userDto);
        log.info("Registration Successful for User: {}", userDto.getUsername());
    }

    @Test
    void testShowRegistrationFormPassword() {
        // Arrange
        Model model = mock(Model.class);

        // Act
        String viewName = authController.showRegistrationFormPassword(model);

        // Assert
        assertEquals("forgot-password", viewName);
        verify(model).addAttribute("user", new UserDto());
        log.info("Password Registration Form Displayed");
    }

    @Test
    void testForgetPasswordSave() {
        // Arrange
        Model model = mock(Model.class);
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("password");
        BindingResult bindingResult = mock(BindingResult.class);

        // Act
        String viewName = authController.forgetPasswordSave(userDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/forgot-password?success", viewName);
        verify(userService).resetPassword("testUser", "password");
        log.info("Password Reset Successful for User: {}", userDto.getUsername());
    }

    @Test
    void testUsers() {
        // Arrange
        Model model = mock(Model.class);
        List<RoleDto> roles = new ArrayList<>();
        List<UserDto> users = new ArrayList<>();
        when(roleService.findAllRoles()).thenReturn(roles);
        when(userService.findAllUsers()).thenReturn(users);

        // Act
        String viewName = authController.users(model);

        // Assert
        assertEquals("users", viewName);
        verify(model).addAttribute("users", users);
        verify(model).addAttribute("roles", roles);
        log.info("User List Viewed");
    }

    @Test
    void testLogin() {
        // Act
        String viewName = authController.login();

        // Assert
        assertEquals("login", viewName);
        log.info("Login Page Viewed");
    }

    @Test
    void testAcceptUser() {
        // Arrange
        int userId = 1;

        // Act
        String viewName = authController.acceptUser(userId);

        // Assert
        assertEquals("redirect:/users", viewName);
        verify(userService).changeUserStatus(userId, "ACCEPTED");
        log.info("User with ID '{}' Accepted", userId);
    }

    @Test
    void testDeclineUser() {
        // Arrange
        int userId = 1;

        // Act
        String viewName = authController.declineUser(userId);

        // Assert
        assertEquals("redirect:/users", viewName);
        verify(userService).changeUserStatus(userId, "DECLINED");
        log.info("User with ID '{}' Declined", userId);
    }

    @Test
    void testChangeUserRole() {
        // Arrange
        int userId = 1;
        String newRole = "ADMIN";

        // Act
        String viewName = authController.changeUserRole(newRole, userId);

        // Assert
        assertEquals("redirect:/users", viewName);
        verify(userService).changeUserRole(newRole, userId);
        log.info("User Role Changed to '{}' for User ID '{}'", newRole, userId);
    }

    @Test
    void testShowRegistrationFormRole() {
        // Arrange
        Model model = mock(Model.class);

        // Act
        String viewName = authController.showRegistrationFormRole(model);

        // Assert
        assertEquals("add-role", viewName);
        verify(model).addAttribute("role", new RoleDto());
        log.info("Role Registration Form Displayed");
    }

    @Test
    void testRegistrationRole() {
        // Arrange
        Model model = mock(Model.class);
        RoleDto roleDto = new RoleDto();
        BindingResult bindingResult = mock(BindingResult.class);

        // Act
        String viewName = authController.registrationRole(roleDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/users", viewName);
        verify(roleService).save(roleDto);
        log.info("Role Registration Successful for Role: {}", roleDto.getName());
    }
}
