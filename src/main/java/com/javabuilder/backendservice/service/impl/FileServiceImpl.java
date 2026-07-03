package com.javabuilder.backendservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.javabuilder.backendservice.dto.response.FileResponse;
import com.javabuilder.backendservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final Cloudinary cloudinary;

    @Override
    public List<FileResponse> uploadFiles(List<MultipartFile> files) {
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
}
