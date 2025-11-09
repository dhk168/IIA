package com.jackson.server.reminder.entity;

import com.jackson.server.reminder.dto.CreateProjectRequest;
import com.jackson.server.reminder.dto.UpdateProjectRequest;
import lombok.Data;

@Data
public class Project {
    private Long projectId;
    private Long userId;
    private String name;
    private String description;
    private String color;
    private String icon;
    private Integer sortOrder;
    private Boolean isArchived;

    public Project() {
    }

    public Project(CreateProjectRequest dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.color = dto.getColor();
        this.icon = dto.getIcon();
    }
}