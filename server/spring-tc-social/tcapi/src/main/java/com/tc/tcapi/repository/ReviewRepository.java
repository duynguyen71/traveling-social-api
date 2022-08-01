package com.tc.tcapi.repository;

import com.tc.core.model.Review;
import com.tc.core.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM review AS r1 \n" +
                    "JOIN (SELECT id FROM review ORDER BY RAND()) as r2 \n" +
                    "ON r1.id=r2.id \n" +
                    "WHERE r1.status = :status\n"
    )
    List<Review> findReviewPostsNative(@Param("status") Integer status, Pageable pageable);

    List<Review> findByUserAndStatus(User user, Integer status, Pageable pageable);

    Optional<Review> findByIdAndUser(Long reqId, User user);

    Optional<Review> findByIdAndStatus(Long reviewId, int status);
}
