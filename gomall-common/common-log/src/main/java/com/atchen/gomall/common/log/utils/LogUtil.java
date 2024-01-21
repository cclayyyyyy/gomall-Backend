package com.atchen.gomall.common.log.utils;

import com.alibaba.fastjson.JSON;
import com.atchen.gomall.common.log.annotation.Log;
import com.atchen.gomall.model.entity.system.SysOperLog;
import com.atchen.gomall.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LogUtil {

    // Invoke after the operation is executed
    public static void afterHandlLog(Log sysLog, Object proceed,
                                     SysOperLog sysOperLog, int status ,
                                     String errorMsg) {
        if(sysLog.isSaveResponseData()) {
            sysOperLog.setJsonResult(JSON.toJSONString(proceed));
        }
        sysOperLog.setStatus(status);
        sysOperLog.setErrorMsg(errorMsg);
    }

    // Invoke before the operation is executed.
    public static void beforeHandleLog(Log sysLog,
                                       ProceedingJoinPoint joinPoint,
                                       SysOperLog sysOperLog) {

        // Set the operation module name
        sysOperLog.setTitle(sysLog.title());
        sysOperLog.setOperatorType(sysLog.operatorType().name());

        // Retrieve information about the target method
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature() ;
        Method method = methodSignature.getMethod();
        sysOperLog.setMethod(method.getDeclaringClass().getName());

        // Retrieve relevant request parameters
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        sysOperLog.setRequestMethod(request.getMethod());
        sysOperLog.setOperUrl(request.getRequestURI());
        sysOperLog.setOperIp(request.getRemoteAddr());

        // Set request parameters
        if(sysLog.isSaveRequestData()) {
            String requestMethod = sysOperLog.getRequestMethod();
            if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
                String params = Arrays.toString(joinPoint.getArgs());
                sysOperLog.setOperParam(params);
            }
        }
        sysOperLog.setOperName(AuthContextUtil.get().getUserName());
    }
}
