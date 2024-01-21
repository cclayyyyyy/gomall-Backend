package com.atchen.gomall.utils;

import com.atchen.gomall.model.entity.system.SysUser;
import com.atchen.gomall.model.entity.user.UserInfo;

public class AuthContextUtil {

    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>() ;


    // 定义存储数据的静态方法
    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    // 定义获取数据的方法
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get() ;
    }

    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }

    // Create a ThreadLocal Object
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    // Add data
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    // Retrieve data
    public static SysUser get() {
        return threadLocal.get();
    }

    // Delete data
    public static void remove() {
        threadLocal.remove();
    }
}
