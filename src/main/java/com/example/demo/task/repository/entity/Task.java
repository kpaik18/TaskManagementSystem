package com.example.demo.task.repository.entity;

import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "seq_task", allocationSize = 1, initialValue = 1000)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "sec_user_id")
    private ApplicationUser applicationUser;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<AttachedFile> attachedFiles;
}
