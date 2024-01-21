package com.atchen.gomall.user.service.impl;

import com.atchen.gomall.model.entity.user.UserAddress;
import com.atchen.gomall.user.mapper.UserAddressMapper;
import com.atchen.gomall.user.service.UserAddressService;
import com.atchen.gomall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    // Retrieve the user's address list
    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        return userAddressMapper.findUserAddressList(userId);
    }

    // Retrieve the shipping address information based on the order id
    @Override
    public UserAddress getUserAddress(Long id) {
        return userAddressMapper.getUserAddress(id);
    }
}
