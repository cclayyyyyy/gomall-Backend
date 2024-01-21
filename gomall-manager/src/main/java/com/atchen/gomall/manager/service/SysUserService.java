package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.dto.system.AssginRoleDto;
import com.atchen.gomall.model.dto.system.LoginDto;
import com.atchen.gomall.model.dto.system.SysUserDto;
import com.atchen.gomall.model.entity.system.SysUser;
import com.atchen.gomall.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

public interface SysUserService {

    //User Login
    LoginVo login(LoginDto loginDto);

    //Retrieve user Info
    SysUser getUserInfo(String token);

    //User logout
    void logout(String token);

    // User conditional pagination query interface
    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    // User creation
    void saveSysUser(SysUser sysUser);

    // User update
    void updateSysUser(SysUser sysUser);

    // User deletion
    void deleteById(Long userId);

    // Assign roles to users
    void doAssign(AssginRoleDto assginRoleDto);
}
