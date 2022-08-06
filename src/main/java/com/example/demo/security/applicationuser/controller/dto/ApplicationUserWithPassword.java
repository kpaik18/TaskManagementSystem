package com.example.demo.security.applicationuser.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationUserWithPassword {
    private Long id;
    private String username;
    private String password;
}
