package com.atchen.gomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atchen.gomall.model.entity.product.Category;
import com.atchen.gomall.product.mapper.CategoryMapper;
import com.atchen.gomall.product.service.CategoryService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // All first-level categories
    @Override
    public List<Category> selectOneCategory() {
        // 1 Query Redis to check if all first-level categories are present
        // key = category:one
        String categoryOneJson = redisTemplate.opsForValue().get("category:one");

        // 2 if yes, return
        if (StringUtils.hasText(categoryOneJson)) {
            // convert string to List<Category>
            List<Category> existCategoryList = JSON.parseArray(categoryOneJson, Category.class);
            return  existCategoryList;
        }

        // 3 if no, query database, return and save data to redis
        List<Category> categoryList = categoryMapper.selectOneCategory();

        redisTemplate.opsForValue().set("category:one",
                JSON.toJSONString(categoryList),
                7, TimeUnit.DAYS);


        return categoryMapper.selectOneCategory();
    }

    // Query all categories and encapsulate them in a tree structure
    // key = category::all
    @Cacheable(value = "category", key = "'all'")
    @Override
    public List<Category> findCategoryTree() {

        // Query all categories
        List<Category> allCategoryList = categoryMapper.findAll();

        // iterate list, parent_id=0: all first-level categories
        List<Category> oneCategoryList = allCategoryList.stream()
                .filter(item -> item.getParentId().longValue() == 0)
                .collect(Collectors.toList());

        // iterate all first-level categories, get second-level categories by id = parent_id

        if(!CollectionUtils.isEmpty(oneCategoryList)) {
            oneCategoryList.forEach(oneCategory -> {
                List<Category> twoCategoryList = allCategoryList.stream()
                        .filter(item -> item.getParentId() == oneCategory.getId())
                        .collect(Collectors.toList());

                // Encapsulate the second-level categories into the first-level categories
                oneCategory.setChildren(twoCategoryList);

                // iterate all second-level categories, get third-level categories by id = parent_id
                if (!CollectionUtils.isEmpty(twoCategoryList)) {
                    twoCategoryList.forEach(twoCategory -> {
                        List<Category> threeCategoryList = allCategoryList.stream()
                                .filter(item -> item.getParentId() == twoCategory.getId())
                                .collect(Collectors.toList());

                        twoCategory.setChildren(threeCategoryList);
                    });
                }
            });
        }


        return oneCategoryList;
    }
}
