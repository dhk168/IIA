package com.jackson.server.debug.service.impl;

import com.jackson.server.debug.mapper.DebugMapper;
import com.jackson.server.debug.service.DebugService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebugServiceImpl implements DebugService {

    private final DebugMapper debugMapper;

    /**
     * 清除所有数据库表中的内容
     * 注意：清除顺序需要考虑外键约束
     */
    @Override
    @Transactional
    public void clearAllData() {
        // 清除auth模块相关表（有外键依赖，需先清除依赖表）
        debugMapper.clearMailTable();
        debugMapper.clearProfileTable();

        // 清除reminder模块相关表（有外键依赖，需先清除关联表）
        debugMapper.clearTaskTagTable(); // 先清除任务标签关联表
        debugMapper.clearTaskTable(); // 然后清除任务表
        debugMapper.clearProjectTable(); // 清除项目表
        debugMapper.clearTagTable(); // 清除标签表
        debugMapper.clearHistoryTable(); // 清除历史表

        // 最后清除auth表（其他表依赖auth表）
        debugMapper.clearAuthTable();

        log.info("所有数据库表内容已成功清除");
    }

    @Override
    @Transactional
    public void clearReminderData() {
        // Clear all reminder-related tables
        debugMapper.clearHistoryTable();
        debugMapper.clearTaskTable();
        debugMapper.clearTaskTagTable();
        debugMapper.clearProjectTable();
        debugMapper.clearTagTable();
    }
}