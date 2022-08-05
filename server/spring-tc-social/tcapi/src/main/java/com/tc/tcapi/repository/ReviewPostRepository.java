package com.tc.tcapi.repository;

import com.tc.core.model.ReviewPost;
import com.tc.core.model.User;
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

    List<ReviewPost> findByUserAndStatus(User user, Integer status, Pageable pageable);

    Optional<ReviewPost> findByIdAndUser(Long reqId, User user);

    Optional<ReviewPost> findByIdAndStatus(Long reviewId, int status);
}
