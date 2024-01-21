package com.atchen.gomall.pay.service;

import com.atchen.gomall.model.entity.pay.PaymentInfo;

import java.util.Map;

public interface PaymentInfoService {

    // Save payment records
    PaymentInfo savePaymentInfo(String orderNo);

    // Update transaction record status for a successful payment
    void updatePaymentStatus(Map<String, String> paramMap);
}
