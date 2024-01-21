package com.atchen.gomall.user.mapper;

import com.atchen.gomall.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

    // 3 Check the username, ensuring it is not duplicated
    UserInfo selectByUsername(String username);

    // User registration
    void save(UserInfo userInfo);
}
