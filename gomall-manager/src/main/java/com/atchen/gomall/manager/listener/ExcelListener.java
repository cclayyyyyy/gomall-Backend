package com.atchen.gomall.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.atchen.gomall.manager.mapper.CategoryMapper;
import com.atchen.gomall.model.vo.product.CategoryExcelVo;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

// Listener, can't be managed by Spring. Prevent concurrent operations from reading the wrong file
public class ExcelListener<T> implements ReadListener<T> {

    /**
     * Store in the database every 5 rows (or as needed, e.g., every 100 rows),
     * and then clear the list to facilitate memory cleanup
     */
    private static final int BATCH_COUNT = 100;
    // Cached data
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    private CategoryMapper categoryMapper;
    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    // Start reading from the second row and encapsulate the content of each row into the T object.
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // Add each row's data for the T object into the `cachedDataList` collection
        cachedDataList.add(t);
        // When reaching the BATCH_COUNT, store the data in the database to prevent OutOfMemory errors
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // Initialize
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // Save data if data size < 100
        saveData();
    }

    // Save to the database
    private void saveData() {
        categoryMapper.batchInsert((List<CategoryExcelVo>)cachedDataList);
    }
}
