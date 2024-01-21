package com.atchen.gomall.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.manager.mapper.SysUserMapper;
import com.atchen.gomall.manager.mapper.SysRoleUserMapper;
import com.atchen.gomall.manager.service.SysUserService;
import com.atchen.gomall.model.dto.system.AssginRoleDto;
import com.atchen.gomall.model.dto.system.LoginDto;
import com.atchen.gomall.model.dto.system.SysUserDto;
import com.atchen.gomall.model.entity.system.SysUser;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //User Login
    @Override
    public LoginVo login(LoginDto loginDto) {

        // Retrieve the input verification code and the key in Redis through LoginDto
        String captcha = loginDto.getCaptcha();
        String key = loginDto.getCodeKey();

        // Query the verification code stored in Redis based on the retrieved key
        String redisCode = redisTemplate.opsForValue().get("user:validate" + key);

        // Compare input Vc with Vc stored in Redis
        // If inconsistent, prompt the user for verification failure
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode, captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // If consistent, delete the verification code from Redis
        redisTemplate.delete("user:validate" + key);

        //1. Get the username from loginDto
        String userName = loginDto.getUserName();

        //2. Query the database table 'sys_user' based on the username
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);

        /**3. If no information is found based on the username,
         * it means the user does not exist.
         In such cases, an error message would be returned. */
        if (sysUser == null) {
//            throw new RuntimeException("Username doesn't exit");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4. If user information is found, the user exists.
        //5. Retrieve the entered password, compare it with the database password to check for consistency
        String database_password = sysUser.getPassword();
        //Encrypt the input password(md5) and then compare it with the database password
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());

        //Compare
        if (!input_password.equals(database_password)) {
//            throw new RuntimeException("Username doesn't exit");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        //6. If the passwords match, the login is successful; if the passwords do not match, the login fails
        //7. Upon successful login, generate a user token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //8. Store the user information of a successful login in Redis
        // key  : token    value  :  user information
        redisTemplate.opsForValue().set(
                "user:login"+token,
                JSON.toJSONString(sysUser),
                7,
                TimeUnit.DAYS);

        //9. Return loginvo Object(especially the token)
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    //Retrieve User Info
    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login" + token);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    //User logout
    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login" + token);
    }

    // User conditional pagination query interface
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserMapper.findByPage(sysUserDto);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    // User creation
    @Override
    public void saveSysUser(SysUser sysUser) {
        // Check that the username is not duplicated
        String userName = sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(userName);
        if (dbSysUser != null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        // Encrypt the input password.
        String md5_password = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(md5_password);

        // set 'status': because it can't be null
        sysUser.setStatus(1);

        sysUserMapper.save(sysUser);
    }

    // User update
    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }

    // User deletion
    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }

    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {
        // 1. Delete previously assigned role data of current user id(userId)
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());
        // 2. Reassign new date
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        // get every user id
        for(Long roleId:roleIdList) {
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(),roleId);
        }
    }
}
