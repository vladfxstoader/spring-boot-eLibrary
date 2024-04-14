package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.RoleDto;
import com.example.eLibrary.model.Role;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RoleMapper {
    public Role map(RoleDto roleDto) {
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setName(roleDto.getName());
        return role;
    }
    public RoleDto map(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }

    public List<Role> mapListToRole(List<RoleDto> roleDtos) {
        return roleDtos.stream().map(roleDto -> map(roleDto)).collect(Collectors.toList());
    }

    public List<RoleDto> mapListToRoleDto(List<Role> roles) {
        return roles.stream().map(role -> map(role)).collect(Collectors.toList());
    }
}
