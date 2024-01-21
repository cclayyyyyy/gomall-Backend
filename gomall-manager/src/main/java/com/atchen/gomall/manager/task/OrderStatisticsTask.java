package com.atchen.gomall.manager.task;

import cn.hutool.core.date.DateUtil;
import com.atchen.gomall.manager.mapper.OrderInfoMapper;
import com.atchen.gomall.manager.mapper.OrderStatisticsMapper;
import com.atchen.gomall.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderStatisticsTask {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    // Every day at 2 AM, query the statistical data for the previous day
    // Add the post-processed data to the statistics result table
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderTotalAmountStatistics() {
        // 1. Retrieve the date of the previous day
        String createDate = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");

        // 2. Perform statistical operations (grouping) based on the date of the previous day
        // Calculate the transaction amount for the previous day
        OrderStatistics orderStatistics = orderInfoMapper.selectStatisticsByDate(createDate);

        // 3. Add the post-processed data to the statistics result table
        if (orderStatistics != null) {
            orderStatisticsMapper.insert(orderStatistics);
        }

    }
}
