package com.jackson.server.debug.controller;

import com.jackson.server.debug.service.DebugService;
import com.jackson.server.utils.ResponseUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final DebugService debugService;

    /**
     * 一键清除所有数据库表中的内容
     * @return 清理结果
     */
    @PostMapping("/clear-all-data")
    public Map<String, Object> clearAllData() {
        try {
            debugService.clearAllData();
            return ResponseUtils.buildSuccessResponse(null, "所有数据库表内容已成功清除");
        } catch (Exception e) {
            log.error("清除数据库表内容失败: ", e);
            return ResponseUtils.buildErrorResponse("清除数据库表内容失败: " + e.getMessage());
        }
    }

    @PostMapping("/clear-reminder-data")
    public Map<String, Object> clearReminderData() {
        try {
            debugService.clearReminderData();
            return ResponseUtils.buildSuccessResponse(null, "所有提醒模块数据表内容已成功清除");
        } catch (Exception e) {
            log.error("清除提醒模块数据表内容失败: ", e);
            return ResponseUtils.buildErrorResponse("清除提醒模块数据表内容失败: " + e.getMessage());
        }
    }
}