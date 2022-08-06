package com.example.demo.security.applicationuser.controller.dto;

import com.example.demo.security.applicationuser.repository.entity.Role;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDTO {
    @NotNull
    private Long id;
    private String username;
    private List<Role> roles;
    private List<RoleGroup> roleGroups;

    public ApplicationUserDTO(Long id, String username){
        this.id = id;
        this.username = username;
    }
}
