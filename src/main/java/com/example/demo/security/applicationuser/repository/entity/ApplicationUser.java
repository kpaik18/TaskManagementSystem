package com.example.demo.security.applicationuser.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sec_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_user_generator")
    @SequenceGenerator(name = "application_user_generator",
            sequenceName = "seq_sec_user",
            allocationSize = 1,
            initialValue = 1000)
    private Long id;

    @NotNull
    private String username;

    private String password;

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
