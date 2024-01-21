package com.atchen.gomall.order.service.impl;

import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.feign.cart.CartFeignClient;
import com.atchen.gomall.feign.product.ProductFeignClient;
import com.atchen.gomall.feign.user.UserFeignClient;
import com.atchen.gomall.model.dto.h5.OrderInfoDto;
import com.atchen.gomall.model.entity.h5.CartInfo;
import com.atchen.gomall.model.entity.order.OrderInfo;
import com.atchen.gomall.model.entity.order.OrderItem;
import com.atchen.gomall.model.entity.order.OrderLog;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.model.entity.user.UserAddress;
import com.atchen.gomall.model.entity.user.UserInfo;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.h5.TradeVo;
import com.atchen.gomall.order.mapper.OrderInfoMapper;
import com.atchen.gomall.order.mapper.OrderItemMapper;
import com.atchen.gomall.order.mapper.OrderLogMapper;
import com.atchen.gomall.order.service.OrderInfoService;
import com.atchen.gomall.utils.AuthContextUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    // Confirm the order
    @Override
    public TradeVo getTrade() {

        // Remote Invocation: retrieve all selected items from the shopping cart
        List<CartInfo> cartInfoList = cartFeignClient.getAllChecked();

        // Create a 'List' to encapsulate order items
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }

        // Retrieve the total payment amount for the order
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }

        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(totalAmount);
        return tradeVo;
    }

    // Generate an order
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {

        // 1 Retrieve all order items from orderInfoDto(List<OrderItem>)
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();

        // 2 If List<OrderItem> is null, throw exception
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        // 3 Check if the product inventory is sufficient
        // Iterate List<OrderItem>, get every orderItem
        // Remote invoke to get SKU info, which includes stock number
        for (OrderItem orderItem : orderItemList) {
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if (productSku == null) {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
            if (orderItem.getSkuNum().intValue() > productSku.getStockNum().intValue()) {
                throw new GuiguException(ResultCodeEnum.STOCK_LESS);
            }
        }

        // 4 Add data to table 'order_info'
        // Encapsulate data to OrderInfo object
        // Remote invoke to get user address
        OrderInfo orderInfo = new OrderInfo();
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        orderInfo.setUserId(userInfo.getId());
        orderInfo.setNickName(userInfo.getNickName());

        // Encapsulate user address
        Long userAddressId = orderInfoDto.getUserAddressId();
        UserAddress userAddress = userFeignClient.getUserAddress(userAddressId);

        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        orderInfoMapper.save(orderInfo);


        // 5 Add data to table 'order_item'
        for (OrderItem orderItem : orderItemList) {
            // Set corresponding order id
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        // 6 Add data to table 'order_log'
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);

        // 7 Remove the items that were used to generate the order from the shopping cart
        cartFeignClient.deleteChecked();

        // 8 return order id
        return orderInfo.getId();
    }

    // Retrieve order information
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getOrderInfoById(orderId);
    }

    // Buy now
    @Override
    public TradeVo buy(Long skuId) {
        List<OrderItem> orderItemList = new ArrayList<>();
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        OrderItem orderItem = new OrderItem();

        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());

        orderItemList.add(orderItem);

        TradeVo tradeVo = new TradeVo();

        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(productSku.getSalePrice());
        return tradeVo;
    }

    // Retrieve a paginated list of orders
    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page,limit);
        // Query order information
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);

        // all order items
        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItemList);
        });
        return new PageInfo<>(orderInfoList);
    }

    // Remote invocation: retrieve order info based on order id
    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getOrderInfoByOrderNo(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    // Update order status
    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {
        OrderInfo orderInfo = orderInfoMapper.getOrderInfoByOrderNo(orderNo);

        // Update order info
        orderInfo.setOrderStatus(1);
        orderInfo.setPayType(orderStatus);
        orderInfo.setPaymentTime(new Date());
        orderInfoMapper.updateById(orderInfo);

        // Record log
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(1);
        orderLog.setNote("Alipay payment successful");
        orderLogMapper.save(orderLog);
    }
}
