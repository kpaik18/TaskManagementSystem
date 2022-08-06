package com.example.demo.task.controller.dto;

import com.example.demo.security.applicationuser.controller.dto.ApplicationUserDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    @NotNull
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private ApplicationUserDTO applicationUserDTO;
}
