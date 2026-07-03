package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileResponse> uploadFiles(List<MultipartFile> files);

}
