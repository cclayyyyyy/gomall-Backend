package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.ProductSpecMapper;
import com.atchen.gomall.manager.service.ProductSpecService;
import com.atchen.gomall.model.entity.product.ProductSpec;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecServiceImpl implements ProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;

    // List
    @Override
    public PageInfo<ProductSpec> findByPage(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<ProductSpec> list = productSpecMapper.findByPage();
        return new PageInfo<>(list);
    }

    // Create
    @Override
    public void save(ProductSpec productSpec) {
        productSpecMapper.save(productSpec);
    }

    // Update
    @Override
    public void updateById(ProductSpec productSpec) {
        productSpecMapper.update(productSpec);
    }

    // Delete
    @Override
    public void deleteById(Long id) {
        productSpecMapper.delete(id);
    }

    @Override
    public List<ProductSpec> findAll() {
        return productSpecMapper.findAll();
    }
}
