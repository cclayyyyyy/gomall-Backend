package com.atchen.gomall.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.model.entity.pay.PaymentInfo;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.pay.service.AlipayService;
import com.atchen.gomall.pay.service.PaymentInfoService;
import com.atchen.gomall.pay.utils.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;

    // Place an Alipay order
    @Override
    public String submitAlipay(String orderNo) {

        // Save payment records
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        // Invoke Alipay service interface
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // Synchronize callback
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // Asynchronous callback
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        // Prepare request parameters, declare a map collection
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount",new BigDecimal("0.01")); // for test
        map.put("subject",paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // Send a request
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()) {
                String form = response.getBody();
                return form;
            } else {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
