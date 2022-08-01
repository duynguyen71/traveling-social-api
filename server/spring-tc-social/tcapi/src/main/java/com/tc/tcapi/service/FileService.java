package com.tc.tcapi.service;

import com.tc.core.model.FileUpload;
import com.tc.tcapi.repository.FileRepository;
import com.tc.tcapi.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService{
    @Autowired
    FileRepository fileRepository;

    public FileUpload findOne(Long id){
        return fileRepository.findById(id).orElse(null);
    }
}
