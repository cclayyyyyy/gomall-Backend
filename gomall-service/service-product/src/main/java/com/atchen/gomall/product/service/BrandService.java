package com.atchen.gomall.product.service;

import com.atchen.gomall.model.entity.product.Brand;

import java.util.List;

public interface BrandService {

    // Retrieve all brands
    List<Brand> findAll();
}
