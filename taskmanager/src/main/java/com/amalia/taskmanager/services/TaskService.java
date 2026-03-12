package com.amalia.taskmanager.services;

import com.amalia.taskmanager.dto.TaskDTO;
import com.amalia.taskmanager.dto.TaskStatsDTO;
import com.amalia.taskmanager.model.Task;
import com.amalia.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository)
    {
        this.taskRepository=taskRepository;
    }
    public Page<TaskDTO> getAllTasks(Pageable pageable)
    {
        return taskRepository.findAll(pageable).map(this::convertToDTO);
    }
    public TaskDTO createTask(TaskDTO dto)
    {
        Task task=convertToEntity(dto);
        Task saved=taskRepository.save(task);
        return convertToDTO(saved);
    }
    public Optional<TaskDTO> getTaskById(Long id)
    {
        return taskRepository.findById(id).map(this::convertToDTO);
    }
    public TaskDTO updateTask(Long id, TaskDTO updatedTask)
    {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());

        if(updatedTask.getStatus()==Task.Status.DONE)
        {
            existingTask.setCompletedAt(LocalDateTime.now());
        }

        Task saved=taskRepository.save(existingTask);

        return convertToDTO(saved);
    }
    public void deleteTaskById(Long id)
    {
        taskRepository.deleteById(id);
    }

    public TaskDTO convertToDTO(Task task)
    {
        return new TaskDTO(task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getStatus(),
                            task.getDueDate()
                );
    }
    private Task convertToEntity(TaskDTO dto)
    {
        Task task=new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        return task;
    }

    public List<TaskDTO> getTasksByStatus(Task.Status status)
    {
        return taskRepository.findByStatus(status).stream().map(this::convertToDTO).toList();
    }

    public TaskDTO findTaskByTitle(String title)
    {
        return  convertToDTO( taskRepository.findByTitle(title));
    }

    public Page<TaskDTO> searchByTitle(String title, Pageable pageable)
    {
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::convertToDTO);
    }

    public TaskStatsDTO getStats()
    {
        long total=taskRepository.count();
        long todo= taskRepository.countByStatus(Task.Status.TODO);
        long inProgress= taskRepository.countByStatus(Task.Status.IN_PROGRESS);
        long done = taskRepository.countByStatus(Task.Status.DONE);

        return new TaskStatsDTO(total,todo,inProgress,done);
    }
}
