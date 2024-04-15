package com.example.eLibrary.controller;

import com.example.eLibrary.dto.RoleDto;
import com.example.eLibrary.dto.UserDto;
import com.example.eLibrary.model.Role;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.RoleService;
import com.example.eLibrary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {
    private UserService userService;
    private RoleService roleService;

    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){
        return "access-denied";
    }

    @GetMapping("/")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("username", name);
        User existingUser = userService.findByUsername(name);
        if(existingUser != null) {
            Role role = existingUser.getRoles().get(0);
            model.addAttribute("role", role.getName());
        }
        else {
            model.addAttribute("role", "none");
        }
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findByUsername(userDto.getUsername().trim());

        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "There is already an account registered with the same username");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.save(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/forgot-password")
    public String showRegistrationFormPassword(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "forgot-password";
    }

    @PostMapping("/forgot-password/save")
    public String forgetPasswordSave(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findByUsername(userDto.getUsername());

        if(existingUser == null || existingUser.getUsername() == null || existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "There is no account registered with this username");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "forgot-password";
        }

        userService.resetPassword(userDto.getUsername(), userDto.getPassword());
        return "redirect:/forgot-password?success";
    }

    @GetMapping("/users")
    public String users(Model model){
        List<RoleDto> roles = roleService.findAllRoles();
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "users";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/accept-user/{id}")
    public String acceptUser(@PathVariable("id") Integer id) {
        userService.changeUserStatus(id, "ACCEPTED");
        return "redirect:/users";
    }

    @PostMapping("/decline-user/{id}")
    public String declineUser(@PathVariable("id") Integer id) {
        userService.changeUserStatus(id, "DECLINED");
        return "redirect:/users";
    }

    @PostMapping("/change-user-role/{id}")
    public String changeUserRole(@RequestParam("newRole") String newRole, @PathVariable("id") Integer id) {
        userService.changeUserRole(newRole, id);
        return "redirect:/users";
    }

    @GetMapping("/add-role")
    public String showRegistrationFormRole(Model model) {
        RoleDto role = new RoleDto();
        model.addAttribute("role", role);
        return "add-role";
    }

    @PostMapping("/add-role/save")
    public String registrationRole(@Valid @ModelAttribute("role") RoleDto roleDto,
                               BindingResult result,
                               Model model) {
        if(result.hasErrors()) {
            model.addAttribute("role", roleDto);
            return "/add-role";
        }
        roleService.save(roleDto);
        return "redirect:/users";
    }
}
