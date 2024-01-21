package com.atchen.gomall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.model.dto.h5.UserLoginDto;
import com.atchen.gomall.model.dto.h5.UserRegisterDto;
import com.atchen.gomall.model.entity.user.UserInfo;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.h5.UserInfoVo;
import com.atchen.gomall.user.mapper.UserInfoMapper;
import com.atchen.gomall.user.service.UserInfoService;
import com.atchen.gomall.utils.AuthContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // User registration
    @Override
    public void register(UserRegisterDto userRegisterDto) {

        // 1 retrieve data through userRegisterDto
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        // 2 Verification code validation
        // 2.1 Get verification code from Redis
        String redisCode = redisTemplate.opsForValue().get(username);
        // 2.2 Compare verification code
        if (!redisCode.equals(code)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // 3 Check the username, ensuring it is not duplicated
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if (userInfo != null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        // 4 Encapsulate data and save it to database
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        userInfoMapper.save(userInfo);

        // 5 Delete verification code from Redis
        redisTemplate.delete(username);

    }

    // User login
    @Override
    public String login(UserLoginDto userLoginDto) {

        // 1 Get username and password from dto
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        // 2 Get user information based on username
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if (userInfo == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 3 Compare password
        String database_password = userInfo.getPassword();
        String md5_password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!database_password.equals(md5_password)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }


        // 4 Check if the current user is disabled
        if (userInfo.getStatus() == 0) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        // 5 Generate a token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        // 6 Put user info into Redis
        redisTemplate.opsForValue().set("user:gomall:"+token,
                                        JSON.toJSONString(userInfo),
                                        30, TimeUnit.DAYS);

        // 7 Return token
        return token;
    }

    // Retrieve information about the currently logged-in user
    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        // Retrive user info from Redis
//        String userJson = redisTemplate.opsForValue().get("user:gomall:" + token);
//        if (!StringUtils.hasText(userJson)) {
//            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
//        }
//        UserInfo userInfo =

        // Retrieve user info from ThreadLocal
        UserInfo userInfo = AuthContextUtil.getUserInfo();

        // userInfo -- UserInfoVo
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        return userInfoVo;
    }
}
