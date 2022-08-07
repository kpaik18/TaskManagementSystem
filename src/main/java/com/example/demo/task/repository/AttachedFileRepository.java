package com.example.demo.task.repository;

import com.example.demo.task.controller.dto.FileDTO;
import com.example.demo.task.repository.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
    List<AttachedFile> findAttachedFileByIsDeletedIsTrue();

    @Query(value = "select new com.example.demo.task.controller.dto.FileDTO(f.name, f.contentType, f.folderPath) " +
            "from attached_file f where f.id = :attachedFileId")
    FileDTO getFileDTOById(Long attachedFileId);
}
