package com.tc.tcapi.repository;

import com.tc.tcapi.model.FileUpload;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByIdAndActive(Long id, Integer active);

    Optional<FileUpload> findByIdAndUploadByAndActive(Long id, User uploadUser, Integer active);

    Optional<FileUpload> findByName(String name);

    Optional<FileUpload> findByIdAndUploadBy(Long id, User uploadBy);

    @Query(nativeQuery = true, value = "SELECT f.* from file_upload f JOIN user u \n" +
            "ON f.upload_by = u.id\n" +
            "JOIN post_content pc ON (pc.attachment_id = f.id AND pc.status = 1)\n" +
//            "LEFT JOIN review_post rvp ON ((rvp.cover_photo_id = f.id) AND rvp.status = 1)\n" +
//            "LEFT JOIN review_post_image rvpi ON (rvpi.image_id = f.id AND rvpi.status =1 )\n" +
            "WHERE f.upload_by = :userId")
    List<FileUpload> getUploadedFiles(@Param("userId") Long userId, Pageable pageRequest);
}
