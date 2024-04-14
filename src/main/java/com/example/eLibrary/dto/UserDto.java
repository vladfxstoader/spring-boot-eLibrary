package com.example.eLibrary.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto
{
    private Integer id;
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Password should not be empty")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String status;
    private String role;
}