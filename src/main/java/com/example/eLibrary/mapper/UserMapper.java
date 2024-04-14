package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.UserDto;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserMapper {
    public User map(UserDto userDto, RoleRepository roleRepository) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setStatus(userDto.getStatus());
        String role = userDto.getRole();
        role = "ROLE_" + role;
        user.setRoles(Arrays.asList(roleRepository.findByNameIgnoreCase(role)));
        return user;
    }

    public UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setStatus(user.getStatus());
        List<Role> roles = user.getRoles();
        String role = roles.get(0).getName().substring(5);
        userDto.setRole(role);
        return userDto;
    }
}
