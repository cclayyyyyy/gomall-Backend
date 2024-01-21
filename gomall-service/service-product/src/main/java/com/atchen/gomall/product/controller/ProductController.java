package com.atchen.gomall.product.controller;

import com.atchen.gomall.model.dto.h5.ProductSkuDto;
import com.atchen.gomall.model.dto.product.SkuSaleDto;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.h5.ProductItemVo;
import com.atchen.gomall.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Update product SKU sales volume")
    @PostMapping("updateSkuSaleNum")
    public Boolean updateSkuSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList) {
        return productService.updateSkuSaleNum(skuSaleDtoList);
    }

    @Operation(summary = "Product details")
    @GetMapping("item/{skuId}")
    public Result item(@PathVariable Long skuId) {
        ProductItemVo productItemVo = productService.item(skuId);
        return  Result.build(productItemVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "Page")
    @GetMapping(value = "/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit,
                       ProductSkuDto productSkuDto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(page,limit,productSkuDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    // Remote Invocation: return sku information based on skuId
    @GetMapping("/getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable Long skuId) {
        ProductSku productSku = productService.getBySkuId(skuId);
        return productSku;
    }


}
