package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.response.FileResponse;
import com.javabuilder.backendservice.dto.response.PreSignedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileResponse> uploadFilesToCloudinary(List<MultipartFile> files);

    FileResponse uploadFileToS3(MultipartFile file) throws IOException;

    PreSignedResponse generatePresignedUrl(String fileName);

    String buildUrl(String key);
}
