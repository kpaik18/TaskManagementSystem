package com.example.demo.task.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "attached_file")
@Data
@NoArgsConstructor
public class AttachedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attached_file_generator")
    @SequenceGenerator(name = "attached_file_generator", sequenceName = "seq_attached_file", allocationSize = 1, initialValue = 1000)
    private Long id;

    private String name;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "folder_path")
    private String folderPath;

    @Column(name = "is_deleted")
    @JsonIgnore
    private Boolean isDeleted;
}
