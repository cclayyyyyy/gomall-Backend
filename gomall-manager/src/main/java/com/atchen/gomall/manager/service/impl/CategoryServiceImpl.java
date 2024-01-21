package com.atchen.gomall.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.manager.listener.ExcelListener;
import com.atchen.gomall.manager.mapper.CategoryMapper;
import com.atchen.gomall.manager.service.CategoryService;
import com.atchen.gomall.model.entity.product.Category;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import com.atchen.gomall.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findCategoryList(Long id) {
        // 1. return "list" based on id
        // select * from category where parent_id=id
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);

        // 2. Determine if each category has subcategories
        // If yes, set hasChildren = true
        if (!CollectionUtils.isEmpty(categoryList)) {
            categoryList.forEach(category -> {
                int count = categoryMapper.selectCountByParentId(category.getId());
                if ( count > 0 ) {
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }
            });
        }
        return categoryList;
    }

    // Export
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 1 Set response header information (mandatory) and other details.
            // Set the response result type
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // Prevent Chinese character encoding issues
            String fileName = URLEncoder.encode("分类数据", "UTF-8");

            // Set response header(fixed format, not to be changed）
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");

            // 2 Query all categories, return "List"
            List<Category> categoryList = categoryMapper.findAll();

            // List<Category> --> List<CategoryExcelVo>
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
//                Long id = category.getId();
//                categoryExcelVo.setId(id);
                BeanUtils.copyProperties(category, categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }

            // 3 Invoke the write method of EasyExcel to complete the writing operation
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }

    // Import
    @Override
    public void importData(MultipartFile file) {
        // Listener
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener)
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
