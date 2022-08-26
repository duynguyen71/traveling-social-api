package com.tc.tcapi.repository;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.Tag;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM review_post AS r1 \n" +
                    "JOIN (SELECT id FROM review_post ORDER BY RAND()) as r2 \n" +
                    "ON r1.id=r2.id \n" +
                    "WHERE r1.status = :status\n"
    )
    List<ReviewPost> findReviewPostsNative(@Param("status") Integer status, Pageable pageable);

    List<ReviewPost> findByUserAndStatus(User user, Integer status);

    Optional<ReviewPost> findByIdAndUser(Long reqId, User user);

    Optional<ReviewPost> findByIdAndStatus(Long reviewId, int status);

    List<ReviewPost> findByTags(Tag tag);

    @Query(nativeQuery = true, value = "SELECT DISTINCT p.* FROM review_post p JOIN review_post_tag pt\n" +
            "ON p.id = pt.review_post_id \n" +
            "JOIN tag t ON t.id = pt.tag_id\n" +
            "WHERE (:tagName IS NULL OR t.name LIKE :tagName)\n" +
            "OR (:title IS NULL OR p.title LIKE :title)")
    List<ReviewPost> searchReviewPostsNative(@Param("tagName") String tagName, @Param("title") String title, Pageable pageable);

    List<ReviewPost> findByVisitors_UserAndVisitors_Status(User user, int status, Pageable pageable);

    int countByUserAndStatus(User user, int status);
}
