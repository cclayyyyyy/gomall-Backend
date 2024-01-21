package com.atchen.gomall.product.service;

import com.atchen.gomall.model.dto.h5.ProductSkuDto;
import com.atchen.gomall.model.dto.product.SkuSaleDto;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    // Sort by sales volume and retrieve the top 10 records
    List<ProductSku> selectProductSkuBySale();

    // Page
    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    // Product details
    ProductItemVo item(Long skuId);

    // Remote Invocation: return sku information based on skuId
    ProductSku getBySkuId(Long skuId);

    // Update product SKU sales volume
    Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
