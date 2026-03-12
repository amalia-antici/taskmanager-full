package com.amalia.taskmanager.dto;

import com.amalia.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min=3, max=100, message = "Title must be 3-100 characters!")
    private String title;

    private String description;

    @NotNull(message = "Status is mandatory")
    private Task.Status status;

    private LocalDateTime dueDate;

    public TaskDTO(Long id, String title, String description, Task.Status status, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public TaskDTO(){}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Task.Status getStatus() {
        return status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
