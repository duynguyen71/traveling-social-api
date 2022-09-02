package com.tc.tcapi.repository;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewPostImageRepository extends JpaRepository<ReviewPostImage,Long> {

    List<ReviewPostImage> findByReviewPostAndStatusOrderByPosAsc(ReviewPost post, int status);

}
