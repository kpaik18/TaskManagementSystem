package com.example.demo.security.rolegroup.repository.entity;

import com.example.demo.security.role.repository.entity.Role;
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
    private Long id;

    @Column(name = "role_group_name")
    private String roleGroupName;

    @ManyToMany
    @JoinTable(
            name = "sec_role_group_sec_role",
            joinColumns = @JoinColumn(name = "sec_role_group_id"),
            inverseJoinColumns = @JoinColumn(name = "sec_role_id")
    )
    private List<Role> roles = new ArrayList<>();
}
