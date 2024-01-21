package com.atchen.gomall.common.log.aspect;

import com.atchen.gomall.common.log.annotation.Log;
import com.atchen.gomall.common.log.service.AsyncOperLogService;
import com.atchen.gomall.common.log.utils.LogUtil;
import com.atchen.gomall.model.entity.system.SysOperLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncOperLogService operLogService;

    // Around advice
    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) {

//        String title = sysLog.title();
//        int businessType = sysLog.businessType();
//        System.out.println("title: "+title+" ::businessType: "+businessType);

        // Before the business method is invoked, encapsulate the data
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);

        // Business method
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();

//            System.out.println("Execute after the business method....");

            // After the business method is invoked, encapsulate the data
            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,0,null);

        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,1,e.getMessage());
            throw new RuntimeException();
        }

        // Invoke the service method to add log information to the database
        operLogService.saveSysOperLog(sysOperLog);
        return proceed;
    }

}
