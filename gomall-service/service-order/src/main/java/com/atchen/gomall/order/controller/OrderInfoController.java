package com.atchen.gomall.order.controller;

import com.atchen.gomall.model.dto.h5.OrderInfoDto;
import com.atchen.gomall.model.entity.order.OrderInfo;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.h5.TradeVo;
import com.atchen.gomall.order.service.OrderInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order management")
@RestController
@RequestMapping(value="/api/order/orderInfo")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    // Update order status
    @GetMapping("auth/updateOrderStatusPayed/{orderNo}")
    public Result updateOrderStatus(@PathVariable(value = "orderNo") String orderNo) {
        orderInfoService.updateOrderStatus(orderNo);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Generate an order
    @Operation(summary = "Submit the order")
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderInfoDto orderInfoDto) {
        Long orderId = orderInfoService.submitOrder(orderInfoDto);
        return Result.build(orderId, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Confirm the order")
    @GetMapping("auth/trade")
    public Result<TradeVo> trade() {
        TradeVo tradeVo = orderInfoService.getTrade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Retrieve order information")
    @GetMapping("auth/{orderId}")
    public Result getOrderInfo(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);
        return Result.build(orderInfo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Buy now")
    @GetMapping("auth/buy/{skuId}")
    public Result buy(@PathVariable Long skuId) {
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Retrieve a paginated list of orders")
    @GetMapping("auth/{page}/{limit}")
    public Result<PageInfo<OrderInfo>> list(@PathVariable Integer page,
                                            @PathVariable Integer limit,
                                            @RequestParam(required = false, defaultValue = "") Integer orderStatus) {
        PageInfo<OrderInfo> pageInfo = orderInfoService.findUserPage(page,limit,orderStatus);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    // Remote invocation: retrieve order info based on order id
    @Operation(summary = "Retrieve order information")
    @GetMapping("auth/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo) {
        OrderInfo orderInfo = orderInfoService.getOrderInfoByOrderNo(orderNo);
        return orderInfo;
    }

}
