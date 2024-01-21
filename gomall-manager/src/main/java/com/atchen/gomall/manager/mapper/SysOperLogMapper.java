package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.entity.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOperLogMapper {
    // Save log data
    void insert(SysOperLog sysOperLog);
}
