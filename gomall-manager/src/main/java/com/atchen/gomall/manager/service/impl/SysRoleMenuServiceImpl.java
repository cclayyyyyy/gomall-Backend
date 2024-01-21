package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.SysRoleMenuMapper;
import com.atchen.gomall.manager.service.SysMenuService;
import com.atchen.gomall.manager.service.SysRoleMenuService;
import com.atchen.gomall.model.dto.system.AssginMenuDto;
import com.atchen.gomall.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    // Query all menus and query menu id that assigned to users
    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        // Query all menus
        List<SysMenu> sysMenuList = sysMenuService.findNodes();

        // Query menu id that assigned to users
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);

        Map<String,Object> map = new HashMap<>();
        map.put("sysMenuList",sysMenuList);
        map.put("roleMenuIds",roleMenuIds);

        return map;
    }

    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        // Delete menu data assigned to users
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());

        // Save reassignment data
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if (menuInfo != null && menuInfo.size() > 0) { //role assigned to a menu before
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
    }
}
