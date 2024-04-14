package com.example.eLibrary.service;

import com.example.eLibrary.dto.RoleDto;
import com.example.eLibrary.mapper.RoleMapper;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public Role save(RoleDto roleDto) {
        Role role = roleMapper.map(roleDto);
        role.setName("ROLE_" + role.getName());
        Role savedRole = roleRepository.save(role);
        return savedRole;
    }

    public Role findByName(String name) {
        return roleRepository.findByNameIgnoreCase(name);
    }

    public List<RoleDto> findAllRoles() {
        return roleMapper.mapListToRoleDto(roleRepository.findAll());
    }
}
