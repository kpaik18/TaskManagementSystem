package com.example.demo.security.applicationuser.controller.dto;

import com.example.demo.security.applicationuser.repository.entity.Role;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class ApplicationUserDTO {
    private Long id;
    private String username;
    private Boolean isPasswordReset;
    private List<Role> roles;
    private List<RoleGroup> roleGroups;
}
