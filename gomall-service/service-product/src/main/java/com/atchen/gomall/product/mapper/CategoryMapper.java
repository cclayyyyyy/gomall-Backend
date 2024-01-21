package com.atchen.gomall.product.mapper;

import com.atchen.gomall.model.entity.product.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // All first-level categories
    List<Category> selectOneCategory();

    // Query all categories
    List<Category> findAll();
}
