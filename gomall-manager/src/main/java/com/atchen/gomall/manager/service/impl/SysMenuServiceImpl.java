package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.manager.mapper.SysMenuMapper;
import com.atchen.gomall.manager.mapper.SysRoleMenuMapper;
import com.atchen.gomall.manager.service.SysMenuService;
import com.atchen.gomall.manager.utils.MenuHelper;
import com.atchen.gomall.model.entity.system.SysMenu;
import com.atchen.gomall.model.entity.system.SysUser;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.system.SysMenuVo;
import com.atchen.gomall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    // Menu List
    @Override
    public List<SysMenu> findNodes() {
        // 1. Query all menus, return a List collection
        List<SysMenu> sysMenuList = sysMenuMapper.findAll();
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }

        // 2. Invoke the method from the utility class
        // encapsulate the returned list collection into the required data format(Element-plus)
        List<SysMenu> treeList = MenuHelper.buildTree(sysMenuList);

        return treeList;
    }

    // Menu Creation
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);

        // For newly added child menus, set the parent menu's 'isHalf' status to half-open： 1
        updateSysRoleMenu(sysMenu);
    }

    // For newly added child menus, set the parent menu's 'isHalf' status to half-open： 1
    private void updateSysRoleMenu(SysMenu sysMenu) {
        // Get parent menu of current added menu
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if (parentMenu != null) {
            // Set the parent menu's 'isHalf' status to half-open： 1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId());
            // Recursive
            updateSysRoleMenu(parentMenu) ;
        }
    }

    // Menu update
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    // Menu deletion
    @Override
    public void removeById(Long id) {
        // Query whether there are submenus based on current menu id
        int count = sysMenuMapper.selectCountById(id);

        // count > 0 include submenus
        if (count > 0) {
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }

        // count = 0, delete
        sysMenuMapper.delete(id);
    }

    // Query the menus that the user can operate on
    @Override
    public List<SysMenuVo> findMenusByUserId() {
        // Get current user id
        SysUser sysUser = AuthContextUtil.get();
        Long userId = sysUser.getId();

        // Query menus that the user can operate on based on user id
        List<SysMenu> sysMenuList = sysMenuMapper.findMenusByUserId(userId);

        // Encapsulate into the target format
        List<SysMenu> sysMenuList1 = MenuHelper.buildTree(sysMenuList);

        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenuList1);
        return sysMenuVos;
    }

    // Convert the List<SysMenu> object to a List<SysMenuVo> object
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
