package com.atchen.gomall.manager.controller;

import com.atchen.gomall.manager.service.CategoryService;
import com.atchen.gomall.model.entity.product.Category;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value="/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Import
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        // Receive the uploaded file
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    // Export
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        categoryService.exportData(response);
    }

    // Lazy loading method
    // Category List, query only one layer of data each time.
    // select * from category where parent_id=id
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable Long id) {
        List<Category> list = categoryService.findCategoryList(id);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }


}
