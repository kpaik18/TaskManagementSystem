package com.example.demo.security.applicationuser.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sec_role_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_group_generator")
    @SequenceGenerator(name = "role_group_generator",
            sequenceName = "seq_sec_role_group",
            allocationSize = 1,
            initialValue = 1000)
    private Long id;

    @Column(name = "role_group_name")
    private String roleGroupName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sec_role_group_sec_role",
            joinColumns = @JoinColumn(name = "sec_role_group_id"),
            inverseJoinColumns = @JoinColumn(name = "sec_role_id")
    )
    private List<Role> roles = new ArrayList<>();
}
