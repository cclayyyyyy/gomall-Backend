package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.dto.product.CategoryBrandDto;
import com.atchen.gomall.model.entity.product.Brand;
import com.atchen.gomall.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService {

    // Conditional pagination query for category brands
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    // Create
    void save(CategoryBrand categoryBrand);

    // Update
    void updateById(CategoryBrand categoryBrand);

    // Delete
    void deleteById(Long id);

    // Query corresponding brand data based on category ID
    List<Brand> findBrandByCategoryId(Long categoryId);
}
