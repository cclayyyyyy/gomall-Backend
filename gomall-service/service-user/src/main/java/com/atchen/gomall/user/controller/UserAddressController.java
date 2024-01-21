package com.atchen.gomall.user.controller;

import com.atchen.gomall.model.entity.user.UserAddress;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User address interface")
@RestController
@RequestMapping(value="/api/user/userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "Retrieve the user's address list")
    @GetMapping("auth/findUserAddressList")
    public Result findUserAddressList() {
        List<UserAddress> list = userAddressService.findUserAddressList();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    // Retrieve the shipping address information based on the order id
    @GetMapping("getUserAddress/{id}")
    public  UserAddress getUserAddress(@PathVariable Long id) {
        return userAddressService.getUserAddress(id);
    }

}
