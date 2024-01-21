package com.atchen.gomall.order.mapper;

import com.atchen.gomall.model.entity.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderInfoMapper {

    // Add data to table 'order_info'
    void save(OrderInfo orderInfo);

    // Retrieve order information
    OrderInfo getOrderInfoById(Long orderId);

    // Query order information
    List<OrderInfo> findUserPage(Long userId, Integer orderStatus);

    // Remote invocation: retrieve order info based on order id
    OrderInfo getOrderInfoByOrderNo(String orderNo);

    // Update order info
    void updateById(OrderInfo orderInfo);
}
