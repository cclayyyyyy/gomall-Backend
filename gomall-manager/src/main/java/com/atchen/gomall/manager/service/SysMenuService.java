package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.entity.system.SysMenu;
import com.atchen.gomall.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysMenuService {

    // Menu List
    List<SysMenu> findNodes();

    // Menu Creation
    void save(SysMenu sysMenu);

    // Menu update
    void update(SysMenu sysMenu);

    // Menu deletion
    void removeById(Long id);

    // Query the menus that the user can operate on
    List<SysMenuVo> findMenusByUserId();
}
