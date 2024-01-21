package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.CategoryBrandMapper;
import com.atchen.gomall.manager.service.CategoryBrandService;
import com.atchen.gomall.model.dto.product.CategoryBrandDto;
import com.atchen.gomall.model.entity.product.Brand;
import com.atchen.gomall.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    // Conditional pagination query for category brands
    @Override
    public PageInfo<CategoryBrand> findByPage(Integer page,
                                              Integer limit,
                                              CategoryBrandDto categoryBrandDto) {

        PageHelper.startPage(page, limit);
        List<CategoryBrand> list = categoryBrandMapper.findByPage(categoryBrandDto);
        PageInfo<CategoryBrand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    // Create
    @Override
    public void save(CategoryBrand categoryBrand) {
        categoryBrandMapper.save(categoryBrand);
    }

    // Update
    @Override
    public void updateById(CategoryBrand categoryBrand) {
        categoryBrandMapper.updateById(categoryBrand);
    }

    // Delete
    @Override
    public void deleteById(Long id) {
        categoryBrandMapper.deleteById(id);
    }

    // Query corresponding brand data based on category ID
    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        return categoryBrandMapper.findBrandByCategoryId(categoryId);
    }
}
