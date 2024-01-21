package com.atchen.gomall.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.atchen.gomall.common.exception.GuiguException;
import com.atchen.gomall.manager.properties.MinioProperties;
import com.atchen.gomall.manager.service.FileUploadService;
import com.atchen.gomall.model.vo.common.ResultCodeEnum;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;


@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                            .build();

            // Make 'gomall-bucket' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                // Make a new bucket called 'gomall-bucket'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {
                System.out.println("Bucket 'gomall-bucket' already exists.");
            }

            // Get uploaded file name
            // Generate unique file name
            // Group the uploaded files based on the current date
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String filename = dateDir + "/" + uuid + file.getOriginalFilename();

            // Upload known sized input stream.
            minioClient.putObject(
                    PutObjectArgs.builder().bucket("gomall-bucket").object("filename").stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .build());

            // Retrieve the path of the uploaded file in Minio
            String url = minioProperties.getEndpointUrl()+"/"+minioProperties.getBucketName()+"/"+filename;

            return url;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
