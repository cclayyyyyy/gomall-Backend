package com.atchen.gomall.order.mapper;

import com.atchen.gomall.model.entity.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    // Add data to table 'order_item'
    void save(OrderItem orderItem);

    // all order items
    List<OrderItem> findByOrderId(Long id);
}
