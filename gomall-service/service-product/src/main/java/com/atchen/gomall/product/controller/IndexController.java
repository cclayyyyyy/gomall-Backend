package com.atchen.gomall.product.controller;

import com.atchen.gomall.model.entity.product.Category;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.h5.IndexVo;
import com.atchen.gomall.product.service.CategoryService;
import com.atchen.gomall.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页接口管理")
@RestController
@RequestMapping(value="/api/product/index")
//@CrossOrigin    //Temporarily address the cross-origin issue
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result index() {
        // 1 All first-level categories
        List<Category> categoryList = categoryService.selectOneCategory();

        // 2 Sort by sales volume and retrieve the top 10 records
        List<ProductSku> productSkuList = productService.selectProductSkuBySale();

        // 3 Encapsulate data into IndexVo
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);

        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }

}
