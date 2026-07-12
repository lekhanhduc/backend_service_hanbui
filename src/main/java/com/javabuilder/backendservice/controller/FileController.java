package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.response.ApiResponse;
import com.javabuilder.backendservice.dto.response.FileResponse;
import com.javabuilder.backendservice.dto.response.PreSignedResponse;
import com.javabuilder.backendservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping
    ApiResponse<List<FileResponse>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        var data = fileService.uploadFilesToCloudinary(files);
        return ApiResponse.<List<FileResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Files uploaded successfully")
                .data(data)
                .build();
    }

    @PostMapping("/s3")
    ApiResponse<FileResponse> uploadFileToS3(@RequestParam("file") MultipartFile file) throws IOException {
        var data = fileService.uploadFileToS3(file);
        return ApiResponse.<FileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Files uploaded successfully")
                .data(data)
                .build();
    }

    @GetMapping("/generate-presigned")
    ApiResponse<PreSignedResponse> generatePresignedUrl(@RequestParam(name = "fileName") String fileName) {
        var data = fileService.generatePresignedUrl(fileName);
        return ApiResponse.<PreSignedResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Presigned URL generated successfully")
                .data(data)
                .build();
    }
}
