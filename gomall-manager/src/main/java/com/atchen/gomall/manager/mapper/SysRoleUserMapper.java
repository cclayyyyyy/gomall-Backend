package com.atchen.gomall.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {
    // delete
    void deleteByUserId(Long userId);

    // assign role to user
    void doAssign(Long userId, Long roleId);

    //query role id assigned to a user based on the userId List
    List<Long> selectRoleIdsByUserId(Long userId);
}
