package com.example.demo.task.controller;

import com.example.demo.beanvalidation.CreateValidation;
import com.example.demo.beanvalidation.UpdateValidation;
import com.example.demo.security.applicationuser.controller.dto.ApplicationUserDTO;
import com.example.demo.security.applicationuser.service.ApplicationUserService;
import com.example.demo.task.controller.dto.AttachedFileDTO;
import com.example.demo.task.controller.dto.AttachedFileList;
import com.example.demo.task.controller.dto.TaskDTO;
import com.example.demo.task.repository.entity.Task;
import com.example.demo.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskResource {

    private final TaskService taskService;
    private final ApplicationUserService applicationUserService;

    @GetMapping
    @RolesAllowed("task_read")
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @RolesAllowed("task_create")
    public ResponseEntity createTask(HttpServletRequest request,
                                     @Validated(CreateValidation.class) AttachedFileList attachedFileList,
                                     @RequestPart @Valid TaskDTO taskDTO) {
        Task task = taskService.createTask(taskDTO, attachedFileList);
        try {
            return ResponseEntity.created(new URI(request.getRequestURL().append("/").append(task.getId().toString()).toString()))
                    .header("Access-Control-Expose-Headers", "location").build();
        } catch (URISyntaxException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    @RolesAllowed("task_update")
    public void updateTask(@PathVariable("id") Long id,
                           @RequestPart @Valid TaskDTO taskDTO,
                           @Validated(UpdateValidation.class) AttachedFileList attachedFileList) {
        taskService.updateTask(id, taskDTO, attachedFileList);
    }

    @DeleteMapping("{id}")
    @RolesAllowed("task_delete")
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }


    @GetMapping("{id}/files")
    @RolesAllowed("task_read")
    public List<AttachedFileDTO> getTaskFiles(@PathVariable("id") Long id) {
        return taskService.getTaskAttachedFiles(id);
    }

    @GetMapping("users")
    @RolesAllowed("task_read")
    public List<ApplicationUserDTO> getUsers(){
        return applicationUserService.getAllApplicationUserDTOs();
    }

}
