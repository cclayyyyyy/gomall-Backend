package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.common.log.service.AsyncOperLogService;
import com.atchen.gomall.manager.mapper.SysOperLogMapper;
import com.atchen.gomall.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    // Save log data
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}
