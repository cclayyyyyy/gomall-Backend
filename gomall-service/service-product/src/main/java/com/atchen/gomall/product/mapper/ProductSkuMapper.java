package com.atchen.gomall.product.mapper;

import com.atchen.gomall.model.dto.h5.ProductSkuDto;
import com.atchen.gomall.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper {

    // Sort by sales volume and retrieve the top 10 records
    List<ProductSku> selectProductSkuBySale();

    // Page
    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    // Get SKU information based on skuId
    ProductSku getById(Long skuId);

    // Get all product sku information based on productId
    List<ProductSku> findByProductId(Long productId);

    // Update product SKU sales volume
    void updateSale(Long skuId, Integer num);
}
