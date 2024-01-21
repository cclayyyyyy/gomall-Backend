package com.atchen.gomall.product.service.impl;

import com.atchen.gomall.model.entity.product.Brand;
import com.atchen.gomall.product.mapper.BrandMapper;
import com.atchen.gomall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    // Retrieve all brands
    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }
}
