package com.jackson.server.reminder.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CreateTaskRequest {
    private Long userId;
    private Long projectId;
    private String title;
    private String description;
    private String category;
    private Long parentTaskId;
    private LocalDateTime dueDate;
    private LocalDateTime startDate;
    private LocalDateTime reminderSentAt;
    private String priority;
    private List<Long> tagIds; // 新增标签ID列表字段
}
