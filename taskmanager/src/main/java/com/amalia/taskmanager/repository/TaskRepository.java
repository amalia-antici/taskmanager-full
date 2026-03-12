package com.amalia.taskmanager.repository;

import com.amalia.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Task.Status status);
    Task findByTitle(String title);
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    long countByStatus(Task.Status status);
}
