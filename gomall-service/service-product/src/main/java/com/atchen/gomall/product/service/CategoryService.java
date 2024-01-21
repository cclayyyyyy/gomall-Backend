package com.atchen.gomall.product.service;

import com.atchen.gomall.model.entity.product.Category;

import java.util.List;

public interface CategoryService {

    // All first-level categories
    List<Category> selectOneCategory();

    // Query all categories and encapsulate them in a tree structure
    List<Category> findCategoryTree();
}
