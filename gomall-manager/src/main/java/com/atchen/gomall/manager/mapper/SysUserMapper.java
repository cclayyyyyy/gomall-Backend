package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.dto.system.SysUserDto;
import com.atchen.gomall.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectUserInfoByUserName(String userName);

    // User conditional pagination query interface
    List<SysUser> findByPage(SysUserDto sysUserDto);

    // User creation
    void save(SysUser sysUser);

    // User update
    void update(SysUser sysUser);

    // User deletion
    void delete(Long userId);
}
