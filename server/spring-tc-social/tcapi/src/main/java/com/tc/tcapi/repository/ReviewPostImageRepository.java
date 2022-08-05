package com.tc.tcapi.repository;

import com.tc.core.model.ReviewPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPostImageRepository extends JpaRepository<ReviewPostImage,Long> {


}
