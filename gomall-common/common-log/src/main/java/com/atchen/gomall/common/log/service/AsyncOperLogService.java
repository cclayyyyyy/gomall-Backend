package com.atchen.gomall.common.log.service;

import com.atchen.gomall.model.entity.system.SysOperLog;

public interface AsyncOperLogService {

    // Save log data
    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
