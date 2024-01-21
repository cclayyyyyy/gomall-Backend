package com.atchen.gomall.cart.service;

import com.atchen.gomall.model.entity.h5.CartInfo;

import java.util.List;

public interface CartService {

    // Add to the shopping cart
    void addToCart(Long skuId, Integer skuNum);

    // Query the shopping cart
    List<CartInfo> getCartList();

    // Delete items from the shopping cart
    void deleteCart(Long skuId);

    // Update the selected status of items in the shopping cart
    void checkCart(Long skuId, Integer isChecked);

    // Update the selected status for all items in the shopping cart
    void allCheckCart(Integer isChecked);

    // Empty the shopping cart
    void clearCart();

    // Remote invocation: Used for displaying selected items from the shopping cart on the checkout page
    List<CartInfo> getAllChecked();

    // Remote invocation: Delete the items from the shopping cart that were used to generate the order
    void deleteChecked();
}
