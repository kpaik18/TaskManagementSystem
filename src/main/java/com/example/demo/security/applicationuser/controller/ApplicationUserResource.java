package com.example.demo.security.applicationuser.controller;

import com.example.demo.security.applicationuser.repository.entity.Role;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import com.example.demo.security.applicationuser.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ApplicationUserResource {
    private final ApplicationUserService applicationUserService;

    @PostMapping
    public void registerApplicationUser() {

    }

    @GetMapping("roles")
    @RolesAllowed("sec_user_read")
    public List<Role> getRoles() {
        return applicationUserService.getAllRoles();
    }

    @GetMapping("rolegroups")
    @RolesAllowed("sec_user_read")
    public List<RoleGroup> getRoleGroups() {
        return applicationUserService.getAllRoleGroups();
    }
}

