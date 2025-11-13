package com.jackson.server.debug.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DebugMapper {

    /**
     * 清除iia_mail表中的所有内容
     */
    @Delete("DELETE FROM iia_mail")
    void clearMailTable();

    /**
     * 清除iia_profile表中的所有内容
     */
    @Delete("DELETE FROM iia_profile")
    void clearProfileTable();

    /**
     * 清除reminder_project表中的所有内容
     */
    @Delete("DELETE FROM reminder_project")
    void clearProjectTable();

    /**
     * 清除reminder_task表中的所有内容
     */
    @Delete("DELETE FROM reminder_task")
    void clearTaskTable();



    /**
     * 清除reminder_history表中的所有内容
     */
    @Delete("DELETE FROM reminder_history")
    void clearHistoryTable();

    /**
     * 清除reminder_tag表中的所有内容
     */
    @Delete("DELETE FROM reminder_tag")
    void clearTagTable();

    /**
     * 清除reminder_task_tag表中的所有内容
     */
    @Delete("DELETE FROM reminder_task_tag")
    void clearTaskTagTable();

    /**
     * 清除iia_auth表中的所有内容
     */
    @Delete("DELETE FROM iia_auth")
    void clearAuthTable();
}