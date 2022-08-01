package com.tc.tcapi.repository;

import com.tc.core.model.FileUpload;
import com.tc.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByIdAndActive(Long id,Integer active);

    Optional<FileUpload> findByIdAndUploadByAndActive(Long id, User uploadUser, Integer active);

    Optional<FileUpload> findByName(String name);

    Optional<FileUpload> findByIdAndUploadBy(Long id, User uploadBy);

}
