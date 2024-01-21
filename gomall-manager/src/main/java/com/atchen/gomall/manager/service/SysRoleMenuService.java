package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.dto.system.AssginMenuDto;

import java.util.Map;

public interface SysRoleMenuService {
    // Query all menus and query menu id that assigned to users
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);


    // Assign menu
    void doAssign(AssginMenuDto assginMenuDto);
}
