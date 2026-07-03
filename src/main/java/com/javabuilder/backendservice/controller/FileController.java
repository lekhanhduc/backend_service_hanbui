package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.response.ApiResponse;
import com.javabuilder.backendservice.dto.response.FileResponse;
import com.javabuilder.backendservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping
    ApiResponse<List<FileResponse>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        var data = fileService.uploadFiles(files);
        return ApiResponse.<List<FileResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Files uploaded successfully")
                .data(data)
                .build();
    }
}
