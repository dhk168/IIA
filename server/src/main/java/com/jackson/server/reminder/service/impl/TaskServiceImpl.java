package com.jackson.server.reminder.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jackson.server.reminder.dto.CreateTaskRequest;
import com.jackson.server.reminder.dto.UpdateTaskRequest;
import com.jackson.server.reminder.entity.Task;
import com.jackson.server.reminder.entity.TaskTag;
import com.jackson.server.reminder.mapper.TaskMapper;
import com.jackson.server.reminder.service.TaskService;
import com.jackson.server.reminder.service.TaskTagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskTagService taskTagService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Task create(Long userId, CreateTaskRequest dto) {
        log.info("Creating task for user: {}", userId);
        // 先设置userId到dto中，再创建Task对象
        dto.setUserId(userId);
        Task task = new Task(dto);
        task.setStatus("todo");
        task.setIsArchived(false);
        Long parentTaskId = task.getParentTaskId();
        int count;
        if(parentTaskId == null){
            count = taskMapper.findByUserIdAndProjectId(userId, null).size();
        }else{
            assert this.getById(parentTaskId).getUserId().equals(userId);
            count = taskMapper.findSubTasks(userId, parentTaskId).size();
        }
        task.setSortOrder(count + 1);

        int result = taskMapper.insert(task);
        if (result > 0) {
            log.info("Task created successfully with id: {}", task.getTaskId());
            
            // 处理标签关联
            if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
                List<TaskTag> taskTags = dto.getTagIds().stream()
                    .map(tagId -> {
                        TaskTag taskTag = new TaskTag();
                        taskTag.setTaskId(task.getTaskId());
                        taskTag.setTagId(tagId);
                        return taskTag;
                    })
                    .collect(Collectors.toList());
                taskTagService.createBatch(taskTags);
                log.info("Created task-tag associations for task id: {}", task.getTaskId());
            }
            
            return task;
        }
        log.error("Failed to create task");
        throw new RuntimeException("Failed to create task");
    }

    @Override
    public List<Task> getAll(Long userId) {
        log.info("Getting all tasks for user: {}", userId);
        return taskMapper.findByUserId(userId);
    }

    @Override
    public Task getById(Long taskId) {
        log.info("Getting task by id: {}", taskId);
        return taskMapper.findById(taskId);
    }

    @Override
    public boolean updateById(Task task) {
        log.info("Updating task with id: {}", task.getTaskId());
        // 验证任务存在且属于当前用户
        Task existingTask = taskMapper.findById(task.getTaskId());
        if (existingTask == null || !existingTask.getUserId().equals(task.getUserId())) {
            log.warn("Task not found or permission denied for task id: {}", task.getTaskId());
            return false;
        }
        
        int result = taskMapper.update(task);
        boolean success = result > 0;
        log.info("Task update {}" + (success ? "successful" : "failed") + " for task id: {}", success ? "successful" : "failed", task.getTaskId());
        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Task updateById(Long userId, UpdateTaskRequest dto) {
        log.info("Updating task for user: {} with dto: {}", userId, dto);
        // 验证任务存在且属于当前用户
        Task existingTask = taskMapper.findById(dto.getTaskId());
        if (existingTask == null || !existingTask.getUserId().equals(userId)) {
            log.warn("Task not found or permission denied for task id: {}", dto.getTaskId());
            throw new RuntimeException("Task not found or permission denied");
        }
        
        // 创建Task对象并设置属性
        Task task = new Task(existingTask);
        task.setProjectId(dto.getProjectId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCategory(dto.getCategory());
        task.setParentTaskId(dto.getParentTaskId());
        task.setDueDate(dto.getDueDate());
        task.setStartDate(dto.getStartDate());
        task.setReminderSentAt(dto.getReminderSentAt());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setIsArchived(dto.getIsArchived());
        task.setSortOrder(dto.getSortOrder());
        
        int result = taskMapper.update(task);
        if (result > 0) {
            log.info("Task updated successfully with id: {}", task.getTaskId());
            
            // 处理标签关联：先删除旧的，再添加新的
            taskTagService.deleteByTaskId(task.getTaskId());
            if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
                List<TaskTag> taskTags = dto.getTagIds().stream()
                    .map(tagId -> {
                        TaskTag taskTag = new TaskTag();
                        taskTag.setTaskId(task.getTaskId());
                        taskTag.setTagId(tagId);
                        return taskTag;
                    })
                    .collect(Collectors.toList());
                taskTagService.createBatch(taskTags);
                log.info("Updated task-tag associations for task id: {}", task.getTaskId());
            }
            
            return task;
        }
        log.error("Failed to update task");
        throw new RuntimeException("Failed to update task");
    }

    @Override
    public boolean updateStatus(Long taskId, String status) {
        log.info("Updating status for task id: {} to {}", taskId, status);
        // 验证任务存在
        Task existingTask = taskMapper.findById(taskId);
        if (existingTask == null) {
            log.warn("Task not found for id: {}", taskId);
            return false;
        }
        
        int result = taskMapper.updateStatus(taskId, status);
        boolean success = result > 0;
        log.info("Task status update {}" + (success ? "successful" : "failed") + " for task id: {}", success ? "successful" : "failed", taskId);
        return success;
    }

    @Override
    public List<Task> getByStatus(Long userId, String status) {
        log.info("Getting tasks for user: {} with status: {}", userId, status);
        return taskMapper.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Task> getByProjectId(Long userId, Long projectId) {
        log.info("Getting tasks for user: {} in project: {}", userId, projectId);
        return taskMapper.findByUserIdAndProjectId(userId, projectId);
    }

    @Override
    public List<Task> getSubTasks(Long userId, Long parentTaskId) {
        log.info("Getting sub-tasks for user: {} with parent task id: {}", userId, parentTaskId);
        return taskMapper.findSubTasks(userId, parentTaskId);
    }

    @Override
    public List<Task> getUpcomingTasks(Long userId, LocalDateTime dueDate) {
        log.info("Getting upcoming tasks for user: {} until: {}", userId, dueDate);
        return taskMapper.findUpcomingTasks(userId, dueDate);
    }
}