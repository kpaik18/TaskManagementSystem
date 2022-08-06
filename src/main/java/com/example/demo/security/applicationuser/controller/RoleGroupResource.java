package com.example.demo.security.applicationuser.controller;

import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import com.example.demo.security.applicationuser.service.RoleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("rolegroups")
@RequiredArgsConstructor
public class RoleGroupResource {

    private final RoleGroupService roleGroupService;

    @GetMapping
    @RolesAllowed("sec_role_group_read")
    public List<RoleGroup> getAllRoleGroups() {
        return roleGroupService.getAllRoleGroups();
    }

    @GetMapping("{id}")
    @RolesAllowed("sec_role_group_read")
    public RoleGroup getRoleGroup(@PathVariable("id") Long id) {
        return roleGroupService.getRoleGroup(id);
    }

    @PostMapping
    @RolesAllowed("sec_role_group_create")
    public ResponseEntity createRoleGroup(HttpServletRequest request, @RequestBody @Valid RoleGroup roleGroup) {
        RoleGroup createdRoleGroup = roleGroupService.createRoleGroup(roleGroup);
        try {
            return ResponseEntity.created(new URI(request.getRequestURL().append("/").append(createdRoleGroup.getId().toString()).toString()))
                    .header("Access-Control-Expose-Headers", "location").build();
        } catch (URISyntaxException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    @RolesAllowed("sec_role_group_update")
    public void updateRoleGroup(@PathVariable("id") Long id,
                                @RequestBody @Valid RoleGroup roleGroup) {
        roleGroupService.updateRoleGroup(id, roleGroup);
    }

    @DeleteMapping("{id}")
    @RolesAllowed("sec_role_group_delete")
    public void deleteRoleGroup(@PathVariable("id") Long id) {
        roleGroupService.deleteRoleGroup(id);
    }
}
