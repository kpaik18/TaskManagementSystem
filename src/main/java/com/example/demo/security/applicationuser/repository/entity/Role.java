package com.example.demo.security.applicationuser.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "sec_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator",
            sequenceName = "seq_sec_role",
            allocationSize = 1,
            initialValue = 1000)
    private Long id;
    private String name;
}
