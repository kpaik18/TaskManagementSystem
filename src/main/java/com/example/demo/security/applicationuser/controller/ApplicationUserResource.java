package com.example.demo.security.applicationuser.controller;

import com.example.demo.security.applicationuser.controller.dto.ApplicationUserDTO;
import com.example.demo.security.applicationuser.controller.dto.ApplicationUserWithPassword;
import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import com.example.demo.security.applicationuser.repository.entity.Role;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import com.example.demo.security.applicationuser.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ApplicationUserResource {
    private final ApplicationUserService applicationUserService;

    @GetMapping
    @RolesAllowed("sec_user_read")
    public List<ApplicationUserDTO> getApplicationUsers() {
        return applicationUserService.getAllApplicationUsers();
    }

    @GetMapping("{id}")
    @RolesAllowed("sec_user_read")
    public ApplicationUserDTO getApplicationUser(@PathVariable("id") Long id){
        return applicationUserService.getApplicationUser(id);
    }

    @PostMapping
    @RolesAllowed("sec_usec_create")
    public ApplicationUserWithPassword registerApplicationUser(@RequestBody ApplicationUser user) {
        return applicationUserService.registerApplicationUser(user);
    }

    @PutMapping("{id}")
    @RolesAllowed("sec_user_update")
    public void updateApplicationUser(@PathVariable("id") Long id,
                                      @RequestBody ApplicationUser user) {
        applicationUserService.updateApplicationUser(id, user);
    }

    @DeleteMapping("{id}")
    @RolesAllowed("sec_user_delete")
    public void deleteApplicationUser(@PathVariable("id") Long id){
        applicationUserService.deleteApplicationUser(id);
    }

    @PatchMapping("{id}/passwordreset")
    @RolesAllowed("sec_user_update")
    public ApplicationUserWithPassword resetApplicationUserPassword(@PathVariable("id") Long id) {
        return applicationUserService.resetUserPassword(id);
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

