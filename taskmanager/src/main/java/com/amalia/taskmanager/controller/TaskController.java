package com.amalia.taskmanager.controller;
import com.amalia.taskmanager.dto.TaskDTO;
import com.amalia.taskmanager.dto.TaskStatsDTO;
import com.amalia.taskmanager.model.Task;
import com.amalia.taskmanager.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService=taskService;
    }
    @GetMapping
    public Page<TaskDTO> getAllTasks( @PageableDefault(size=5, sort="createdAt") Pageable pageable)
    {
        return taskService.getAllTasks(pageable);
    }
    @PostMapping
    public TaskDTO createTask(@Valid @RequestBody TaskDTO task)
    {
        return taskService.createTask(task);
    }
    @GetMapping("/status/{status}")
    public List<TaskDTO> getByStatus(@PathVariable Task.Status status)
    {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,@Valid @RequestBody TaskDTO updatedTask)
    {
        try {
            TaskDTO task=taskService.updateTask(id, updatedTask);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id)
    {
        taskService.deleteTaskById(id);
    }

    @GetMapping("/search")
    public Page<TaskDTO> searchTasksByTitle(@RequestParam String title, Pageable pageable)
    {
        return taskService.searchByTitle(title, pageable);
    }
    @GetMapping("/stats")
    public TaskStatsDTO getStats()
    {
        return taskService.getStats();
    }
}
