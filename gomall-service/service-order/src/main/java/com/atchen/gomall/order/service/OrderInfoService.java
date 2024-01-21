package com.atchen.gomall.order.service;

import com.atchen.gomall.model.dto.h5.OrderInfoDto;
import com.atchen.gomall.model.entity.order.OrderInfo;
import com.atchen.gomall.model.vo.h5.TradeVo;
import com.github.pagehelper.PageInfo;

public interface OrderInfoService {

    // Confirm the order
    TradeVo getTrade();

    // Generate an order
    Long submitOrder(OrderInfoDto orderInfoDto);

    // Retrieve order information
    OrderInfo getOrderInfo(Long orderId);

    // Buy now
    TradeVo buy(Long skuId);

    // Retrieve a paginated list of orders
    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);

    // Remote invocation: retrieve order info based on order id
    OrderInfo getOrderInfoByOrderNo(String orderNo);

    // Update order status
    void updateOrderStatus(String orderNo);
}
