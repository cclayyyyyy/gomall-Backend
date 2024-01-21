package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.ProductUnitMapper;
import com.atchen.gomall.manager.service.ProductUnitService;
import com.atchen.gomall.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll();
    }
}
