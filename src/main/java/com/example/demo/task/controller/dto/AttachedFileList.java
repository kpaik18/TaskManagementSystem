package com.example.demo.task.controller.dto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


public class AttachedFileList {
    private List<@Valid AttachedFileDTO> attachedFiles = new ArrayList<>();

    public List<AttachedFileDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<AttachedFileDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }
}
