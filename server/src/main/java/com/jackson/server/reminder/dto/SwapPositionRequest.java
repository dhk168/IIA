package com.jackson.server.reminder.dto;

import com.jackson.server.utils.ColorUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SwapPositionRequest {
    private Long projectId;
    private Integer sortOrder;
}