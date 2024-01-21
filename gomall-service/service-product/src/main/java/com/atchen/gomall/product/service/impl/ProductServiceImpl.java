package com.atchen.gomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atchen.gomall.model.dto.h5.ProductSkuDto;
import com.atchen.gomall.model.dto.product.SkuSaleDto;
import com.atchen.gomall.model.entity.product.Product;
import com.atchen.gomall.model.entity.product.ProductDetails;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.model.vo.h5.ProductItemVo;
import com.atchen.gomall.product.mapper.ProductDetailsMapper;
import com.atchen.gomall.product.mapper.ProductMapper;
import com.atchen.gomall.product.mapper.ProductSkuMapper;
import com.atchen.gomall.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    // Sort by sales volume and retrieve the top 10 records
    @Override
    public List<ProductSku> selectProductSkuBySale() {
        return productSkuMapper.selectProductSkuBySale();
    }

    // Page
    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page,limit);
        List<ProductSku> list = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(list);
    }

    // Product details
    @Override
    public ProductItemVo item(Long skuId) {

        // 1 Create a ProductItemVo to encapsulate final data
        ProductItemVo productItemVo = new ProductItemVo();

        // 2 Get SKU information based on skuId
        ProductSku productSku = productSkuMapper.getById(skuId);

        // 3 Based on the SKU, retrieve the productId, and then obtain the product information
        Long productId = productSku.getProductId();
        Product product = productMapper.getById(productId);

        // 4 Get product detail information based on productId
        ProductDetails productDetails = productDetailsMapper.getByProductId(productId);

        // 5 Encapsulat a map == Product specifications corresponding to product SKU ID information
        Map<String, Object> skuSpecValueMap = new HashMap<>();
        // Get all product sku information based on productId
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item -> {
            skuSpecValueMap.put(item.getSkuSpec(),item.getId());
        });

        // 6 Encapsulate the required information
        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);

        String imageUrls = productDetails.getImageUrls();
        String[] split = imageUrls.split(",");
        List<String> list = Arrays.asList(split);
        productItemVo.setDetailsImageUrlList(list);

        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));

        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));

        return productItemVo;
    }

    // Remote Invocation: return sku information based on skuId
    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku productSku = productSkuMapper.getById(skuId);
        return productSku;
    }

    // Update product SKU sales volume
    @Transactional
    @Override
    public Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        if(!CollectionUtils.isEmpty(skuSaleDtoList)) {
            for(SkuSaleDto skuSaleDto : skuSaleDtoList) {
                productSkuMapper.updateSale(skuSaleDto.getSkuId(), skuSaleDto.getNum());
            }
        }
        return true;
    }
}
