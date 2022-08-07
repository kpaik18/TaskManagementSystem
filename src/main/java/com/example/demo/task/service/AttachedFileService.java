package com.example.demo.task.service;

import com.example.demo.task.controller.dto.AttachedFileDTO;
import com.example.demo.task.repository.AttachedFileRepository;
import com.example.demo.task.repository.entity.AttachedFile;
import com.example.demo.task.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SortComparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
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

    public List<AttachedFile> saveAttachedFiles(List<AttachedFileDTO> files, Task task) {
        List<AttachedFile> savedFiles = new ArrayList<>();
        for (AttachedFileDTO attachedFileDTO : files) {
            savedFiles.add(saveAttachedFile(attachedFileDTO, task));
        }
        return savedFiles;
    }

    public AttachedFile saveAttachedFile(AttachedFileDTO attachedFileDTO, Task task) {
        MultipartFile file = attachedFileDTO.getFile();

        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setContentType(file.getContentType());
        attachedFile.setName(attachedFileDTO.getName());
        attachedFile.setTask(task);
        attachedFile.setFolderPath(FOLDER_PATH);
        attachedFile.setIsDeleted(false);
        attachedFileRepository.saveAndFlush(attachedFile);

        Path taskFolder = Paths.get(FOLDER_PATH);
        Path attachedFilePath = taskFolder.resolve(attachedFile.getId().toString());
        try {
            saveFile(file, attachedFilePath, taskFolder);
        } catch (IOException ex) {
            throw new RuntimeException("can't create file");
        }
        return attachedFile;
    }

    // every hour deletion
    @Scheduled(cron = "0 0 * * * *")
    public void taskDeleteJob(){
        List<AttachedFile> toDeleteFiles = attachedFileRepository.findAttachedFileByIsDeletedIsTrue();

        for (AttachedFile fileAttachment : toDeleteFiles) {
            try {
                deleteFile(Path.of(fileAttachment.getFolderPath() + File.separator + fileAttachment.getId()));
                attachedFileRepository.delete(fileAttachment);
                attachedFileRepository.flush();
            } catch (PersistenceException | IOException ignored) {

            }
        }
    }

    public static void deleteFile(Path attachmentPath) throws IOException {
        Files.deleteIfExists(attachmentPath);
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
