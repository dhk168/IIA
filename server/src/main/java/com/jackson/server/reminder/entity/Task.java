package com.jackson.server.reminder.entity;

import java.time.LocalDateTime;
import com.jackson.server.reminder.dto.CreateTaskRequest;
import com.jackson.server.reminder.dto.UpdateTaskRequest;
import lombok.Data;

@Data
public class Task {
    private Long taskId; // AUTO_INCREMENT
    private Long userId; // NOT NULL
    
    // 显式添加getter方法以确保可访问性
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    private Long projectId; // NULL
    
    // 核心元数据
    private String title; // NOT NULL
    private String description;
    private String category; // 'task' 或 'note' NOT NULL DEFAULT 'task'
    private String status; // 'todo', 'done' 或 'abandoned' NOT NULL DEFAULT 'todo'
    private Boolean isArchived; // DEFAULT FALSE
    
    // 层级结构
    private Long parentTaskId; // NULL
    private Integer sortOrder;
    
    // 时间信息
    private LocalDateTime dueDate;
    private LocalDateTime startDate;
    private LocalDateTime completedAt;
    private LocalDateTime reminderSentAt;
    
    // 优先级
    private String priority; // 'none', 'low', 'medium', 'high'

    public Task() {
    }

    public Task(CreateTaskRequest dto) {
        this.userId = dto.getUserId();
        this.projectId = dto.getProjectId();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.category = dto.getCategory();
        this.parentTaskId = dto.getParentTaskId();
        this.dueDate = dto.getDueDate();
        this.startDate = dto.getStartDate();
        this.reminderSentAt = dto.getReminderSentAt();
        this.priority = dto.getPriority();
    }
    
    public Task(Task task) {
        this.taskId = task.getTaskId();
        this.userId = task.getUserId();
        this.projectId = task.getProjectId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.category = task.getCategory();
        this.status = task.getStatus();
        this.isArchived = task.getIsArchived();
        this.parentTaskId = task.getParentTaskId();
        this.sortOrder = task.getSortOrder();
        this.dueDate = task.getDueDate();
        this.startDate = task.getStartDate();
        this.completedAt = task.getCompletedAt();
        this.reminderSentAt = task.getReminderSentAt();
        this.priority = task.getPriority();
    }

    public Task(UpdateTaskRequest dto){
        this.projectId = dto.getProjectId();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.category = dto.getCategory();
        this.parentTaskId = dto.getParentTaskId();
        this.dueDate = dto.getDueDate();
        this.startDate = dto.getStartDate();
        this.reminderSentAt = dto.getReminderSentAt();
        this.priority = dto.getPriority();
    }
}