package com.atchen.gomall.manager.service;

import com.atchen.gomall.model.entity.product.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    // Category List, query only one layer of data each time.
    List<Category> findCategoryList(Long id);

    // Export
    void exportData(HttpServletResponse response);

    // Import
    void importData(MultipartFile file);
}
