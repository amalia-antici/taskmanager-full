package com.amalia.taskmanager.dto;

public class TaskStatsDTO {
    private long total;
    private long todo;
    private long inProgress;
    private long done;

    public TaskStatsDTO(long total, long todo, long inProgress, long done) {
        this.total = total;
        this.todo = todo;
        this.inProgress = inProgress;
        this.done = done;
    }

    public long getTotal() {
        return total;
    }

    public long getTodo() {
        return todo;
    }

    public long getInProgress() {
        return inProgress;
    }

    public long getDone() {
        return done;
    }
}
