package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserRepositoryUnitTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Create a user
        User user = new User();
        user.setId(1);
        user.setUsername("test_user");

        // Mocking the repository behavior
        when(userRepository.findByUsername("test_user")).thenReturn(user);

        // Call the findByUsername method
        User foundUser = userRepository.findByUsername("test_user");

        // Assertion
        assertEquals(user, foundUser);
        log.info("User found: {}", foundUser);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Mocking the repository behavior for not found case
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);

        // Call the findByUsername method for not found case
        User foundUser = userRepository.findByUsername("unknown_user");

        // Assertion
        assertEquals(null, foundUser);
        log.info("User not found for username: unknown_user");
    }
}
