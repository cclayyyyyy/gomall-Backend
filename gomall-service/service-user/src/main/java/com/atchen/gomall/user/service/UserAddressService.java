package com.atchen.gomall.user.service;

import com.atchen.gomall.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {

    // Retrieve the user's address list
    List<UserAddress> findUserAddressList();

    // Retrieve the shipping address information based on the order id
    UserAddress getUserAddress(Long id);
}
