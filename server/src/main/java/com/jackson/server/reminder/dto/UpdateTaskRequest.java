package com.jackson.server.reminder.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    private Long taskId;
    private Long projectId;
    private String title;
    private String description;
    private String category;
    private Long parentTaskId;
    private LocalDateTime dueDate;
    private LocalDateTime startDate;
    private LocalDateTime reminderSentAt;
    private String priority;
    private List<Long> tagIds; // 标签ID列表字段
    private String status;
    private Boolean isArchived;
    private Integer sortOrder;
}