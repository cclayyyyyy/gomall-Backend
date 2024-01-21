package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.SysRoleMapper;
import com.atchen.gomall.manager.mapper.SysRoleUserMapper;
import com.atchen.gomall.manager.service.SysRoleService;
import com.atchen.gomall.model.dto.system.SysRoleDto;
import com.atchen.gomall.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {
        // Set paging parameters
        PageHelper.startPage(current, limit);
        // Query all data based on conditions
        List<SysRole> list = sysRoleMapper.findByPage(sysRoleDto);
        // Encapsulate pageInfo
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    // Method for role addition
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    // Method for updating role
    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    // Method for deleting role
    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }

    // Read all roles
    @Override
    public Map<String, Object> findAll(Long userId) {
        // 1 read all roles
        List<SysRole> roleList = sysRoleMapper.findAll();

        // 2 assigned roles
        //query role id assigned to a user based on the userId List
        List<Long> roleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("allRolesList", roleList);
        map.put("sysUserRoles", roleIds);

        return map;
    }
}
