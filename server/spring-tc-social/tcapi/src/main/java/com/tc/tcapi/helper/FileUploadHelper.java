package com.tc.tcapi.helper;

import com.tc.core.exception.FileUploadException;
import com.tc.tcapi.model.FileUpload;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.FileUploadResponse;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("FileUploadHelper")
@RequiredArgsConstructor
@Slf4j
public class FileUploadHelper {

    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> uploadFile(MultipartFile file) {
        try {
            FileUploadResponse fileUploadResponse = fileStorageService.saveImage(file);
            log.info("Upload image success {}", fileUploadResponse.toString());
            return BaseResponse.success(fileUploadResponse, "Upload file success!");
        } catch (FileUploadException e) {
            return ResponseEntity.badRequest().body(BaseResponse.badRequest(e.getMessage()));
        }
    }

    public ResponseEntity<?> uploadFiles(List<MultipartFile> files) {
        try {
            List<FileUploadResponse> result = new ArrayList<>();
            for (MultipartFile file : files) {
                result.add(fileStorageService.saveImage(file));
            }
            return BaseResponse.success(result, "Upload file success!");
        } catch (FileUploadException e) {
            return ResponseEntity.badRequest().body(BaseResponse.badRequest(e.getMessage()));
        }
    }

    public ResponseEntity<?> getUploadedFiles(Long userId, Map<String,String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageRequest = baseParamRequest.toPageRequest();
        List<FileUpload> files = fileStorageService.getUploadedFiles(userId,pageRequest);
        List<FileUploadResponse> data = files.stream().map(fileUpload -> modelMapper.map(fileUpload, FileUploadResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(data, "Get user " + userId + " file upload success!");
    }
}
