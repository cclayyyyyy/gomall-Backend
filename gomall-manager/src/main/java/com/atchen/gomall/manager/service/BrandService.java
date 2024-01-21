package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.entity.product.Brand;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BrandService {

    // List
    PageInfo<Brand> findByPage(Integer page, Integer limit);

    // Create
    void save(Brand brand);

    // Update
    void updateById(Brand brand);

    // Delete
    void deleteById(Long id);

    // Query all brand
    List<Brand> findAll();
}
