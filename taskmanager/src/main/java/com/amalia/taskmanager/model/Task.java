package com.amalia.taskmanager.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Tasks")
public class Task {

    public enum Status{
        TODO,
        IN_PROGRESS,
        DONE
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;

    public Task()
    {
        this.createdAt=LocalDateTime.now();
        this.status=Status.TODO;
    }

    public Task(Long id, String title, String description, LocalDateTime dueDate, LocalDateTime completedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = Status.TODO   ;
        this.createdAt = LocalDateTime.now();
        this.dueDate = dueDate;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {

        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
