package com.example.demo.task.service;

import com.example.demo.security.applicationuser.controller.dto.ApplicationUserDTO;
import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import com.example.demo.security.applicationuser.service.ApplicationUserService;
import com.example.demo.task.controller.dto.AttachedFileList;
import com.example.demo.task.controller.dto.TaskDTO;
import com.example.demo.task.repository.TaskRepository;
import com.example.demo.task.repository.entity.AttachedFile;
import com.example.demo.task.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final AttachedFileService attachedFileService;
    private final ApplicationUserService applicationUserService;

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(t -> new TaskDTO(t.getId(), t.getName(), t.getDescription(),
                        new ApplicationUserDTO(t.getApplicationUser().getId(), t.getApplicationUser().getUsername()))).collect(Collectors.toList());
    }

    public Task createTask(TaskDTO taskDTO, AttachedFileList attachedFileList) {
        Task task = new Task();
        ApplicationUser applicationUser = applicationUserService.lookupUser(taskDTO.getApplicationUserDTO().getId());
        task.setApplicationUser(applicationUser);
        task.setDescription(taskDTO.getDescription());
        task.setName(taskDTO.getName());
        taskRepository.save(task);
        if (attachedFileList != null) {
            List<AttachedFile> attachedFiles = attachedFileService.saveAttachedFiles(attachedFileList.getAttachedFiles());
            task.setAttachedFiles(attachedFiles);
        }
        return task;
    }

    private Task lookupTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new RuntimeException("can't find task");
        }
        return optionalTask.get();
    }

    public void updateTask(Long id, TaskDTO taskDTO) {
        Task task = lookupTask(id);
        task.setDescription(taskDTO.getDescription());
        task.setName(taskDTO.getName());
        ApplicationUser user = applicationUserService.lookupUser(taskDTO.getApplicationUserDTO().getId());
        task.setApplicationUser(user);
    }

    public void deleteTask(Long id) {
        Task task = lookupTask(id);
        taskRepository.delete(task);
    }
}
