package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.dto.system.AssginMenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    // Query menu id that assigned to users
    List<Long> findSysRoleMenuByRoleId(Long roleId);

    // Delete menu data assigned to users
    void deleteByRoleId(Long roleId);

    // Save reassignment data
    void doAssign(AssginMenuDto assginMenuDto);

    // Set the parent menu's 'isHalf' status to half-open： 1
    void updateSysRoleMenuIsHalf(Long menuId);
}
