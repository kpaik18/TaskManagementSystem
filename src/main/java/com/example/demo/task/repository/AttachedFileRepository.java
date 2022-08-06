package com.example.demo.task.repository;

import com.example.demo.task.repository.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
}