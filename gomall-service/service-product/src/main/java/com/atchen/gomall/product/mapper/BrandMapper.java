package com.atchen.gomall.product.mapper;

import com.atchen.gomall.model.entity.product.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {

    // Retrieve all brands
    List<Brand> findAll();
}
