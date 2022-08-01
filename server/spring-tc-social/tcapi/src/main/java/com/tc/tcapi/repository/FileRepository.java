package com.tc.tcapi.repository;

import com.tc.core.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long> {
    Optional<FileUpload> findById(Long id);
}
