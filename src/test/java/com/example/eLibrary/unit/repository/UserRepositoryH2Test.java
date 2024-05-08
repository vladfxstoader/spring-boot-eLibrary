package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
class UserRepositoryH2Test {

    @Autowired
    UserRepository userRepository;

    @Test
    void getUsers() {
        List<User> users = userRepository.findAll();
        assertNotNull(users);

        log.info("getUsers...");
        users.forEach(user -> {
            log.info("Username: {}", user.getUsername());
            log.info("First name: {}", user.getFirstName());
            log.info("Last name: {}", user.getLastName());
            log.info("Role: {}", user.getRoles());
        });
    }

    @Test
    void testFindByUsername() {
        String username = "user3";

        User foundUser = userRepository.findByUsername(username);

        if (foundUser != null) {
            log.info("Found user:");
            log.info("User ID: {}", foundUser.getId());
            log.info("User Name: {}", foundUser.getUsername());
        } else {
            log.info("No user found with the username '{}'.", username);
        }
    }
}
