package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.BrandMapper;
import com.atchen.gomall.manager.service.BrandService;
import com.atchen.gomall.model.entity.product.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    // List
    @Override
    public PageInfo<Brand> findByPage(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<Brand> list = brandMapper.findByPage();
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    // Create
    @Override
    public void save(Brand brand) {
        brandMapper.save(brand);
    }

    // Update
    @Override
    public void updateById(Brand brand) {
        brandMapper.updateById(brand);
    }

    // Delete
    @Override
    public void deleteById(Long id) {
        brandMapper.deleteById(id) ;
    }

    // Query all brand
    @Override
    public List<Brand> findAll() {
        return brandMapper.findByPage();
    }
}
