package com.atchen.gomall.manager.mapper;

import com.atchen.gomall.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {

    // 3. Save product details to "product_details" table
    void save(ProductDetails productDetails);

    // 3. Delete product details based on id: product_details
    ProductDetails findProductDetailsById(Long id);

    // Update "product_details"
    void updateById(ProductDetails productDetails);

    // Delete "product_details" relevant information based on product id
    void deleteByProductId(Long id);
}
