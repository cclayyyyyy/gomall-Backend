package com.atchen.gomall.cart.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atchen.gomall.cart.service.CartService;
import com.atchen.gomall.feign.product.ProductFeignClient;
import com.atchen.gomall.model.entity.h5.CartInfo;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    private String getCartKey(Long userId) {
        //定义key user:cart:userId
        return "user:cart:" + userId;
    }

    // Add to the shopping cart
    @Override
    public void addToCart(Long skuId, Integer skuNum) {

        // 1 Must be in a logged-in state, retrieve the current user's ID(from ThreadLocal),
        // and use it as the key value for the Redis hash type
        Long userId = AuthContextUtil.getUserInfo().getId();

        String cartKey = this.getCartKey(userId);

        // 2 Retrieve cart data from redis based on user ID and skuId(key + field)
        // key: userId      field: skuId     value: SKU information(CartInfo)
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));

        // 3 If the product is already added to the shopping cart, add the quantities
        CartInfo cartInfo = null;
        if (cartInfoObj != null) {
            cartInfo = JSON.parseObject(cartInfoObj.toString(),CartInfo.class);
            // Add
            cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
            // Set properties: default selected state
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            // 4 If the product is not in the cart, add the product to the shopping cart(redis)
            // And retrieve product information based on skuId -- Nacos + OpenFeign(because there's no mysql operation in this module)

            cartInfo = new CartInfo();

            // Remote invocation: retrieve product information based on skuId
            ProductSku productSku = productFeignClient.getBySkuId(skuId);
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        // Add to Redis
        redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));
    }

    // Query the shopping cart
    @Override
    public List<CartInfo> getCartList() {

        // 1 Query key from Redis based on userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // 2 Retrieve value from Redis
        List<Object> valueList = redisTemplate.opsForHash().values(cartKey);

        // List<Object> --> List<CartInfo>
        if (!CollectionUtils.isEmpty(valueList)) {
            List<CartInfo> cartInfoList = valueList.stream().map(cartInfoObj ->
                            JSON.parseObject(cartInfoObj.toString(), CartInfo.class))
                    .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                    .collect(Collectors.toList());
            return cartInfoList;
        }

        return new ArrayList<>();
    }

    // Delete items from the shopping cart
    @Override
    public void deleteCart(Long skuId) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        redisTemplate.opsForHash().delete(cartKey, String.valueOf(skuId));
    }

    // Update the selected status of items in the shopping cart
    @Override
    public void checkCart(Long skuId, Integer isChecked) {

        // 1 Query key from Redis based on userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // 2 Check if the key contains a field
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey, String.valueOf(skuId));
        if (hasKey) {
            // 3 Retrieve value
            String cartInfoString = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)).toString();

            // 4 Update the selected status of the value
            CartInfo cartInfo = JSON.parseObject(cartInfoString, CartInfo.class);
            cartInfo.setIsChecked(isChecked);

            // 5 Put it back
            redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId), JSON.toJSONString(cartInfo));
        }



    }

    // Update the selected status for all items in the shopping cart
    @Override
    public void allCheckCart(Integer isChecked) {

        // 1 Query key from Redis based on userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // 2 Retrieve all values
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        if (!CollectionUtils.isEmpty(objectList)) {
            List<CartInfo> cartInfoList = objectList.stream().map(object ->
                    JSON.parseObject(object.toString(), CartInfo.class))
                    .collect(Collectors.toList());

            // 3 Update 'isChecked'
            cartInfoList.forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                redisTemplate.opsForHash().put(cartKey, String.valueOf(cartInfo.getSkuId()), JSON.toJSONString(cartInfo));
            });
        }



    }

    // Empty the shopping cart
    @Override
    public void clearCart() {

        // 1 Query key from Redis based on userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // Delete data based on Redis
        redisTemplate.delete(cartKey);

    }

    // Remote invocation: Used for displaying selected items from the shopping cart on the checkout page
    @Override
    public List<CartInfo> getAllChecked() {
        // Retrieve userId, create key
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // Retrieve all selected items from the shopping cart
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        if (!CollectionUtils.isEmpty(objectList)) {
            List<CartInfo> cartInfoList = objectList.stream().map(object ->
                            JSON.parseObject(object.toString(), CartInfo.class))
                            .filter(cartInfo -> cartInfo.getIsChecked()==1)
                            .collect(Collectors.toList());
            return cartInfoList;
        }

        return new ArrayList<>();
    }

    // Remote invocation: Delete the items from the shopping cart that were used to generate the order
    @Override
    public void deleteChecked() {
        // Retrieve userId, create key
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // Retrieve values from Redis based on key
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        // Delete checked items
        objectList.stream().map(object -> JSON.parseObject(object.toString(), CartInfo.class))
                .filter(cartInfo -> cartInfo.getIsChecked()==1)
                .forEach(cartInfo -> redisTemplate.opsForHash().delete(cartKey,String.valueOf(cartInfo.getSkuId())));

    }
}
