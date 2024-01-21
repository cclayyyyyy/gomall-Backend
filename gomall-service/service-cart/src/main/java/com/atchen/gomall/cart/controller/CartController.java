package com.atchen.gomall.cart.controller;

import com.atchen.gomall.cart.service.CartService;
import com.atchen.gomall.model.entity.h5.CartInfo;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/order/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary="Empty the shopping cart")
    @GetMapping("/auth/clearCart")
    public Result clearCart(){
        cartService.clearCart();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary="Update the selected status for all items in the shopping cart")
    @GetMapping("/auth/allCheckCart/{isChecked}")
    public Result allCheckCart(@Parameter(name = "isChecked", description = "是否选中 1:选中 0:取消选中", required = true) @PathVariable(value = "isChecked") Integer isChecked) {
        cartService.allCheckCart(isChecked);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary="Update the selected status of items in the shopping cart")
    @GetMapping("/auth/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@Parameter(name = "skuId", description = "商品skuId", required = true) @PathVariable(value = "skuId") Long skuId,
                            @Parameter(name = "isChecked", description = "是否选中 1:选中 0:取消选中", required = true) @PathVariable(value = "isChecked") Integer isChecked) {
        cartService.checkCart(skuId,isChecked);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Delete items from the shopping cart")
    @DeleteMapping("auth/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(skuId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Query the shopping cart")
    @GetMapping("auth/cartList")
    public Result cartList() {
        List<CartInfo> cartInfoList = cartService.getCartList();
        return Result.build(cartInfoList, ResultCodeEnum.SUCCESS);
    }

    // skuId: Product SKU's ID value
    // skuNum: Quantity of the product
    @Operation(summary = "Add to the shopping cart")
    @GetMapping("auth/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable Long skuId,
                            @PathVariable Integer skuNum) {
        cartService.addToCart(skuId,skuNum);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Remote invocation: Used for displaying selected items from the shopping cart on the checkout page
    @Operation(summary="Selected items in the shopping cart")
    @GetMapping(value = "/auth/getAllCkecked")
    public List<CartInfo> getAllChecked() {
        List<CartInfo> cartInfoList = cartService.getAllChecked() ;
        return cartInfoList;
    }

    // Remote invocation: Delete the items from the shopping cart that were used to generate the order
    @GetMapping("/auth/deleteChecked")
    public Result deleteChecked() {
        cartService.deleteChecked();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
