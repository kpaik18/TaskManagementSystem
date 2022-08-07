package com.example.demo.task.controller.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileDTO {
    private Resource fileResource;
    private String name;
    private String contentType;
    private String folderPath;

    public FileDTO(String name, String contentType, String folderPath) {
        this.name = name;
        this.contentType = contentType;
        this.folderPath = folderPath;
    }
}
