package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.dto.order.OrderStatisticsDto;
import com.atchen.gomall.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
