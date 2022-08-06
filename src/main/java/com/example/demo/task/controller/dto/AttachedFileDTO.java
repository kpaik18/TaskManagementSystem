package com.example.demo.task.controller.dto;

import com.example.demo.task.repository.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AttachedFileDTO {
    private MultipartFile file;
    private Long id;
    private String name;
    private Task task;
}
