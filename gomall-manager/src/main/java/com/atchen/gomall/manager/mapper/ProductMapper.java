package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.dto.product.ProductDto;
import com.atchen.gomall.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    // List and page
    List<Product> findByPage(ProductDto productDto);

    // 1. Save product information to the "product" table
    void save(Product product);

    // 1. Query product information based on id: product
    Product findProductById(Long id);

    // Update "product
    void updateById(Product product);

    // Delete "product" relevant information based on product id
    void deleteById(Long id);
}
