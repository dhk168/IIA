package com.jackson.server.debug.service;

public interface DebugService {

    /**
     * 清除所有数据库表中的内容
     */
    void clearAllData();
    
    /**
     * 清除所有提醒模块的数据表
     */
    void clearReminderData();
}