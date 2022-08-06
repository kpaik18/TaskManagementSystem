package com.example.demo.task.controller.dto;

import java.util.ArrayList;
import java.util.List;


public class AttachedFileList {
    private List<AttachedFileDTO> attachedFiles = new ArrayList<>();

    public List<AttachedFileDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<AttachedFileDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }
}
