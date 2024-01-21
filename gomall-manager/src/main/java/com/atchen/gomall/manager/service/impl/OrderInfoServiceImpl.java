package com.atchen.gomall.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.atchen.gomall.manager.mapper.OrderStatisticsMapper;
import com.atchen.gomall.manager.service.OrderInfoService;
import com.atchen.gomall.model.dto.order.OrderStatisticsDto;
import com.atchen.gomall.model.entity.order.OrderStatistics;
import com.atchen.gomall.model.vo.order.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        // Query statistical result data based on DTO conditions and return a list collection
        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.selectList(orderStatisticsDto);

        // Iterate through the list collection, obtain all dates,
        // and encapsulate all dates into a new list collection

        List<String> dateList = orderStatisticsList.stream()
                .map(orderStatistics -> DateUtil.format(orderStatistics.getOrderDate(),"yyyy-MM-dd"))
                .collect(Collectors.toList());

        // Iterate through the list collection, obtain the total amount corresponding to each date,
        // and encapsulate all dates into a new list collection
        List<BigDecimal> decimalList = orderStatisticsList.stream()
                .map(OrderStatistics::getTotalAmount)
                .collect(Collectors.toList());

        // Encapsulate the two list collections into OrderStatisticsVo and return OrderStatisticsVo
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(decimalList);

        return orderStatisticsVo;
    }
}
