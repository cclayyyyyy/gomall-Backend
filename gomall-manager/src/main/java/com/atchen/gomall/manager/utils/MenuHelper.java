package com.atchen.gomall.manager.utils;

import com.atchen.gomall.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

// Encapsulate tree-like menu data.
public class MenuHelper {

    // Implement encapsulation process recursively
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {

        List<SysMenu> trees = new ArrayList<>();
        //Traverse
        for (SysMenu sysMenu:sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }

        return trees;
    }

    // Recursive
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren((new ArrayList<>()));
        for (SysMenu it: sysMenuList) {
            if (sysMenu.getId().longValue() == it.getParentId().longValue()) {
                sysMenu.getChildren().add(findChildren(it,sysMenuList));
            }
        }
        return sysMenu;
    }
}
