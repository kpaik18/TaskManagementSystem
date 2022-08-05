package com.example.demo.security.applicationuser.repository.entity;

import com.example.demo.security.role.repository.entity.Role;
import com.example.demo.security.rolegroup.repository.entity.RoleGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sec_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
    @Id
    private Long id;
    private String username;
    private String password;
    @Column(name = "is_password_reset")
    private Boolean isPasswordReset;
    @ManyToMany
    @JoinTable(
            name = "sec_user_sec_role",
            joinColumns = @JoinColumn(name = "sec_user_id"),
            inverseJoinColumns = @JoinColumn(name = "sec_role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "sec_user_sec_role_group",
            joinColumns = @JoinColumn(name = "sec_user_id"),
            inverseJoinColumns = @JoinColumn(name = "sec_role_group_id")
    )
    private List<RoleGroup> roleGroups = new ArrayList<>();
}
