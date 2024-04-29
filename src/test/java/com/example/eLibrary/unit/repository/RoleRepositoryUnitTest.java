package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Role;
import com.example.eLibrary.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RoleRepositoryUnitTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testFindByNameIgnoreCase() {
        // Create a role
        Role role = new Role();
        role.setId(1);
        role.setName("TEST_ROLE");

        // Mocking the repository behavior
        when(roleRepository.findByNameIgnoreCase("test_role")).thenReturn(role);

        // Call the findByNameIgnoreCase method
        Role foundRole = roleRepository.findByNameIgnoreCase("test_role");

        // Assertion
        assertEquals(role, foundRole);
        log.info("Role found: {}", foundRole);
    }

    @Test
    void testFindByNameIgnoreCase_NotFound() {
        // Mocking the repository behavior for not found case
        when(roleRepository.findByNameIgnoreCase("unknown_role")).thenReturn(null);

        // Call the findByNameIgnoreCase method for not found case
        Role foundRole = roleRepository.findByNameIgnoreCase("unknown_role");

        // Assertion
        assertNull(foundRole);
        log.info("Role not found for name: unknown_role");
    }
}
