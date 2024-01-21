package com.atchen.gomall.manager.controller;

import com.atchen.gomall.manager.service.FileUploadService;
import com.atchen.gomall.model.vo.common.Result;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) { // name can only be 'file'

        // 1 get uploaded file
        // 2 Invoke the service method for uploading, and return the Minio path
        String url = fileUploadService.upload(file);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }
}
