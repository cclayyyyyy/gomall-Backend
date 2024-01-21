package com.atchen.gomall.manager.test;

import com.alibaba.excel.EasyExcel;
import com.atchen.gomall.model.vo.product.CategoryExcelVo;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {

    public static void main(String[] args) {
        read();
        write();
    }

    // Read
    public static void read(){
        // 1 define the excel file location
        String fileName = "C://01.xlsx";

        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener();
        EasyExcel.read(fileName, CategoryExcelVo.class, excelListener)
                .sheet().doRead();
        List<CategoryExcelVo> data = excelListener.getData();
        System.out.println(data);
    }

    // Write
    public static void write(){
        List<CategoryExcelVo> list = new ArrayList<>();
        list.add(new CategoryExcelVo(1L , "数码办公" , "",0L, 1, 1)) ;
        list.add(new CategoryExcelVo(11L , "华为手机" , "",1L, 1, 2)) ;
        EasyExcel.write("C://02.xlsx", CategoryExcelVo.class)
                .sheet("Classification data").doWrite(list);
    }
}
