package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Role;
import com.example.eLibrary.repository.RoleRepository;
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
class RoleRepositoryH2Test {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void getRoles() {
        List<Role> roles = roleRepository.findAll();
        assertNotNull(roles);

        log.info("getRoles...");
        roles.forEach(role -> log.info(role.getName()));
    }

    @Test
    void testFindByNameIgnoreCase() {
        String roleName = "ROLE_USER";

        Role foundRole = roleRepository.findByNameIgnoreCase(roleName);

        if (foundRole != null) {
            log.info("Found role:");
            log.info("Role ID: {}", foundRole.getId());
            log.info("Role Name: {}", foundRole.getName());
        } else {
            log.info("No role found with the name '{}'.", roleName);
        }
    }
}
