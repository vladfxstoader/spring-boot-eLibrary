package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.RoleDto;
import com.example.eLibrary.mapper.RoleMapper;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.repository.RoleRepository;
import com.example.eLibrary.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RoleServiceUnitTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testSave() {
        // Arrange
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("USER");
        Role role = new Role();
        when(roleMapper.map(roleDto)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role savedRole = roleService.save(roleDto);

        // Assert
        assertEquals(role, savedRole);
        verify(roleRepository, times(1)).save(role);

        log.info("Role saved successfully");
    }

    @Test
    void testFindByName() {
        // Arrange
        String roleName = "USER";
        Role role = new Role();
        when(roleRepository.findByNameIgnoreCase(roleName)).thenReturn(role);

        // Act
        Role foundRole = roleService.findByName(roleName);

        // Assert
        assertEquals(role, foundRole);
        verify(roleRepository, times(1)).findByNameIgnoreCase(roleName);

        log.info("Find by name method test passed successfully");
    }

    @Test
    void testFindAllRoles() {
        // Arrange
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("USER");
        roles.add(role1);

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ADMIN");
        roles.add(role2);

        when(roleRepository.findAll()).thenReturn(roles);

        RoleDto roleDto1 = new RoleDto();
        roleDto1.setId(1);
        roleDto1.setName("USER");

        RoleDto roleDto2 = new RoleDto();
        roleDto2.setId(2);
        roleDto2.setName("ADMIN");

        when(roleMapper.mapListToRoleDto(roles)).thenReturn(List.of(roleDto1, roleDto2));

        // Act
        List<RoleDto> foundRoles = roleService.findAllRoles();

        // Assert
        assertEquals(2, foundRoles.size());
        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).mapListToRoleDto(roles);

        log.info("Find all roles method passed successfully");
    }
}
