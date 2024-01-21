package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.dto.product.ProductDto;
import com.atchen.gomall.model.entity.product.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService {

    // List and page
    PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto);

    // Create product information
    void save(Product product);

    // Query product information based on product id
    Product getById(Long id);

    // Save modified data
    void update(Product product);

    // Delete
    void deleteById(Long id);

    // Product audit
    void updateAuditStatus(Long id, Integer auditStatus);

    // Product listing and delisting
    void updateStatus(Long id, Integer status);
}
