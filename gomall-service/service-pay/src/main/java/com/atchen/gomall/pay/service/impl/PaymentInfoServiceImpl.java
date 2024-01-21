package com.atchen.gomall.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.atchen.gomall.feign.order.OrderFeignClient;
import com.atchen.gomall.feign.product.ProductFeignClient;
import com.atchen.gomall.model.dto.product.SkuSaleDto;
import com.atchen.gomall.model.entity.order.OrderInfo;
import com.atchen.gomall.model.entity.order.OrderItem;
import com.atchen.gomall.model.entity.pay.PaymentInfo;
import com.atchen.gomall.pay.mapper.PaymentInfoMapper;
import com.atchen.gomall.pay.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    // Save payment records
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {

        // 1 Retrieve payment records based on the order number
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);

        // 2 Determine the existence of payment records
        if (paymentInfo == null) {
            // Remote Invocation to retrieve order info
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
            // Encapsulate order info to paymentInfo
            paymentInfo = new PaymentInfo();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            String content = "";
            for(OrderItem item : orderInfo.getOrderItemList()) {
                content += item.getSkuName() + " ";
            }
            paymentInfo.setContent(content);
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);
            // Add
            paymentInfoMapper.save(paymentInfo);
        }

        return paymentInfo;
    }

    // Update transaction record status for a successful payment
    @Override
    public void updatePaymentStatus(Map<String, String> map) {
        // map.get("out_trade_no")
        // 1 Retrieve payment record information based on the order number
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(map.get("out_trade_no"));

        // 2 No need to update if the payment record is already completed
        if (paymentInfo.getPaymentStatus()==1) {
            return;
        }

        // 3 Update required as it is not completed
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(map.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(map));
        paymentInfoMapper.updatePaymentInfo(paymentInfo);

        // Update order status
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo());

        // Update SKU sales volume
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo());
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).collect(Collectors.toList());
        productFeignClient.updateSkuSaleNum(skuSaleDtoList) ;

    }

}
