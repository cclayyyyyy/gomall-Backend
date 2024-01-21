package com.atchen.gomall.manager.controller;

import com.atchen.gomall.common.log.annotation.Log;
import com.atchen.gomall.manager.service.SysRoleService;
import com.atchen.gomall.model.dto.system.SysRoleDto;
import com.atchen.gomall.model.entity.system.SysRole;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    // Read all roles
    @GetMapping("/findAllRoles/{userId}")
    public Result findAllRoles(@PathVariable("userId") Long userId) {
        // String for all roles, Object for assigned roles
        Map<String,Object> map = sysRoleService.findAll(userId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    // Method for role deletion
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId) {
        sysRoleService.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Method for role update
    @PutMapping("/updateSysRole")
    public  Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Method for role addition
    @Log(title = "Role management: Add", businessType = 1)
    @PostMapping(value = "/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Method for retrieving a list of roles
    // current: current page
    // limit: Number of records displayed per page.
    // SysRoleDto: Encapsulate the condition role name object.
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current") Integer current,
                             @PathVariable("limit") Integer limit,
                             @RequestBody SysRoleDto sysRoleDto) {
        // pageHelper plugin implements paging
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto, current, limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
