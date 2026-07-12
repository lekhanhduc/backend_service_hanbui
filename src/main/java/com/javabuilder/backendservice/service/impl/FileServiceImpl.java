package com.javabuilder.backendservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.javabuilder.backendservice.dto.response.FileResponse;
import com.javabuilder.backendservice.dto.response.PreSignedResponse;
import com.javabuilder.backendservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "FILE-SERVICE")
public class FileServiceImpl implements FileService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private final Cloudinary cloudinary;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public List<FileResponse> uploadFilesToCloudinary(List<MultipartFile> files) {
        if(files.isEmpty()) {
            return List.of();
        }
        List<FileResponse> response = new ArrayList<>();
        files.forEach(file -> {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            long size = file.getSize();
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "backend-service",
                        "use_filename", true,
                        "overwrite", false
                ));
                String secureUrl = (String) uploadResult.get("secure_url");

                response.add(FileResponse.builder()
                                .fileName(fileName)
                                .fileType(fileType)
                                .size(size)
                                .url(secureUrl)
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return response;
    }

    @Override
    public FileResponse uploadFileToS3(MultipartFile file) throws IOException {
        String key = generateKey(file.getOriginalFilename());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(key) // java-logo.png -->  java-logo_7848743.png
                .bucket(bucketName)
                .contentType(file.getContentType())
                .build();
        RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);
        if(putObjectResponse.sdkHttpResponse().isSuccessful()) {
            log.info("Uploaded file to S3 successfully");
        }

        String url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
        return FileResponse.builder()
                .fileName(key)
                .fileType(file.getContentType())
                .size(file.getSize())
                .url(url)
                .displayOrder(1)
                .build();
    }

    @Override
    public PreSignedResponse generatePresignedUrl(String fileName) {
        String key = generateKey(fileName);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(key)
                .bucket(bucketName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);
        String preSignedUrl = presignedPutObjectRequest.url().toExternalForm();
        return PreSignedResponse.builder()
                .objectKey(key)
                .preSignedUrl(preSignedUrl)
                .build();
    }

    @Override
    public String buildUrl(String key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }

    private String generateKey(String originalFilename) {
        if(!StringUtils.hasText(originalFilename)) {
            return System.currentTimeMillis() + "";
        } else {
            return originalFilename.substring(0, originalFilename.lastIndexOf("."))
                    + "_" + System.currentTimeMillis()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));
        }
    }
}
