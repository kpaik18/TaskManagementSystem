package com.example.demo.task.controller.dto;

import com.example.demo.beanvalidation.CreateValidation;
import com.example.demo.beanvalidation.UpdateValidation;
import com.example.demo.task.repository.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachedFileDTO {
    @NotNull(groups = CreateValidation.class)
    private MultipartFile file;
    private Long id;
    @NotNull(groups = {CreateValidation.class, UpdateValidation.class})
    private String name;
    private Task task;

    public AttachedFileDTO(Long id, String name, Task task) {
        this.id = id;
        this.name = name;
        this.task = task;
    }
}
