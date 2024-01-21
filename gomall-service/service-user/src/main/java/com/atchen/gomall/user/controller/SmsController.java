package com.atchen.gomall.user.controller;

import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping(value = "/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone) {
        smsService.sendCode(phone);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
