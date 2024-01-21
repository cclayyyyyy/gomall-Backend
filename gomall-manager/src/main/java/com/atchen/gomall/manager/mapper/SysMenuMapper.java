package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    // Query all menus
    List<SysMenu> findAll();

    // Menu Creation
    void save(SysMenu sysMenu);

    // Menu update
    void update(SysMenu sysMenu);

    // Query whether there are submenus based on current menu id
    int selectCountById(Long id);

    // Delete
    void delete(Long id);

    // Query menus that the user can operate on based on user id
    List<SysMenu> findMenusByUserId(Long userId);

    // Get parent menu of current added menu
    SysMenu selectParentMenu(Long parentId);
}
