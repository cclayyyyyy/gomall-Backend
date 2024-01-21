package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.dto.system.SysRoleDto;
import com.atchen.gomall.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    // Method for role addition
    void save(SysRole sysRole);

    // Method for role update
    void update(SysRole sysRole);

    void delete(Long roleId);

    List<SysRole> findAll();
}
