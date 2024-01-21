package com.atchen.gomall.manager.controller;

import com.atchen.gomall.manager.service.ProductService;
import com.atchen.gomall.model.dto.product.ProductDto;
import com.atchen.gomall.model.entity.product.Product;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/admin/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Product listing and delisting
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    // Product audit
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id,
                                    @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    // Delete
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@Parameter(name = "id", description = "商品id", required = true) @PathVariable Long id) {
        productService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    // Save modified data
    @PutMapping("/updateById")
    public Result update(@RequestBody Product product) {
        productService.update(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Query product information based on product id
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.build(product,ResultCodeEnum.SUCCESS);
    }

    // Create product information
    @PostMapping("/save")
    public Result save(@RequestBody Product product) {
        productService.save(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // List and page
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit,
                       ProductDto productDto) {
        PageInfo<Product> pageInfo = productService.findByPage(page,limit,productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
