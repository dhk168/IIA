package com.jackson.server.reminder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新任务状态的请求参数
 */
@Data
public class UpdateTaskStatusRequest {
    
    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    /**
     * 任务状态
     */
    @NotNull(message = "任务状态不能为空")
    private String status;
}