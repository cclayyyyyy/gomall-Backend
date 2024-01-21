package com.atchen.gomall.manager.service.impl;

import com.atchen.gomall.manager.mapper.ProductDetailsMapper;
import com.atchen.gomall.manager.mapper.ProductMapper;
import com.atchen.gomall.manager.mapper.ProductSkuMapper;
import com.atchen.gomall.manager.service.ProductService;
import com.atchen.gomall.model.dto.product.ProductDto;
import com.atchen.gomall.model.entity.product.Product;
import com.atchen.gomall.model.entity.product.ProductDetails;
import com.atchen.gomall.model.entity.product.ProductSku;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    // List and page
    @Override
    public PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        PageHelper.startPage(page,limit);
        List<Product> list = productMapper.findByPage(productDto);
        return new PageInfo<>(list);
    }

    // Create product information
    @Override
    public void save(Product product) {
        // 1. Save product information to the "product" table
        product.setStatus(0);
        product.setAuditStatus(0);
        productMapper.save(product);

        // 2. Retrieve the list collection of product SKUs,
        // and save the SKU information to the "product_sku" table
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (int i = 0; i < productSkuList.size(); i++) {
            ProductSku productSku = productSkuList.get(i);

            // Product code
            productSku.setSkuCode(product.getId()+"_"+i);
            // Product id
            productSku.setProductId(product.getId());
            // skuName
            productSku.setSkuName(product.getName()+productSku.getSkuSpec());
            // Sales volume
            productSku.setSaleNum(0);
            productSku.setStatus(0);

            productSkuMapper.save(productSku);
        }

        // 3. Save product details to "product_details" table
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);
    }

    // Query product information based on product id
    @Override
    public Product getById(Long id) {

        // 1. Query product information based on id: product
        Product product = productMapper.findProductById(id);

        // 2. Query SKU information based on id: product_sku
        List<ProductSku> productSkuList = productSkuMapper.findProductSkuByProductId(id);
        product.setProductSkuList(productSkuList);

        // 3. Delete product details based on id: product_details
        ProductDetails productDetails = productDetailsMapper.findProductDetailsById(id);
        String imageUrls = productDetails.getImageUrls();
        product.setDetailsImageUrls(imageUrls);


        return product;
    }

    // Save modified data
    @Override
    public void update(Product product) {
        // Update "product
        productMapper.updateById(product);

        // Update "product_sku"
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuList.forEach(productSku -> {
            productSkuMapper.updateById(productSku);
        });

        // Update "product_details"
        String detailsImageUrls = product.getDetailsImageUrls();
        ProductDetails productDetails = productDetailsMapper.findProductDetailsById(product.getId());
        productDetails.setImageUrls(detailsImageUrls);
        productDetailsMapper.updateById(productDetails);
    }

    // Delete
    @Override
    public void deleteById(Long id) {
        // 1. Delete "product" relevant information based on product id
        productMapper.deleteById(id);

        // 2. Delete "product_sku" relevant information based on product id
        productSkuMapper.deleteByProductId(id);

        // 3. Delete "product_details" relevant information based on product id
        productDetailsMapper.deleteByProductId(id);
    }

    // Product audit
    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);

    }

    // Product listing and delisting
    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }
}
