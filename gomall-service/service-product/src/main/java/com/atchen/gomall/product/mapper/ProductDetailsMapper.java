package com.atchen.gomall.product.mapper;

import com.atchen.gomall.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {

    // Get product detail information based on productId
    ProductDetails getByProductId(Long productId);
}
