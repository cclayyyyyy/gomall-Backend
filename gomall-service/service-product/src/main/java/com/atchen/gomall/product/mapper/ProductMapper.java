package com.atchen.gomall.product.mapper;

import com.atchen.gomall.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    // Retrieve the productId, and then obtain the product information
    Product getById(Long productId);
}
