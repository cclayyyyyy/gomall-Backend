package com.atchen.gomall.order.mapper;

import com.atchen.gomall.model.entity.order.OrderLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogMapper {

    // Add data to table 'order_log'
    void save(OrderLog orderLog);
}
