package com.atchen.gomall.pay.mapper;

import com.atchen.gomall.model.entity.pay.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInfoMapper {

    // 1 Retrieve payment records based on the order number
    PaymentInfo getByOrderNo(String orderNo);

    // Add order info
    void save(PaymentInfo paymentInfo);

    void updatePaymentInfo(PaymentInfo paymentInfo);
}
