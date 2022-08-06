package com.example.demo.task.service;

import com.example.demo.task.controller.dto.AttachedFileDTO;
import com.example.demo.task.repository.AttachedFileRepository;
import com.example.demo.task.repository.entity.AttachedFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachedFileService {

    private static final String FOLDER_PATH =
            "C:{}Users{}Surface{}Desktop{}Meama{}Files".replace("{}", File.separator);
    private final AttachedFileRepository attachedFileRepository;

    public List<AttachedFile> saveAttachedFiles(List<AttachedFileDTO> files) {
        List<AttachedFile> savedFiles = new ArrayList<>();
        for (AttachedFileDTO attachedFileDTO : files) {
            savedFiles.add(saveAttachedFile(attachedFileDTO));
        }
        return savedFiles;
    }

    public AttachedFile saveAttachedFile(AttachedFileDTO attachedFileDTO) {
        MultipartFile file = attachedFileDTO.getFile();

        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setContentType(file.getContentType());
        attachedFile.setName(attachedFileDTO.getName());
        attachedFile.setTask(attachedFileDTO.getTask());
        attachedFile.setFolderPath(FOLDER_PATH);
        attachedFileRepository.save(attachedFile);

        Path taskFolder = Paths.get(FOLDER_PATH);
        Path attachedFilePath = taskFolder.resolve(attachedFile.getId().toString());
        try {
            saveFile(file, attachedFilePath, taskFolder);
        } catch (IOException ex) {
            throw new RuntimeException("can't create file");
        }
        return attachedFile;
    }

    private void saveFile(MultipartFile file, Path attachmentPath, Path folder) throws IOException {
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        try {
            Files.copy(file.getInputStream(), attachmentPath);
        } catch (IOException e) {
            throw new RuntimeException("file can't be created");
        }
    }
}
