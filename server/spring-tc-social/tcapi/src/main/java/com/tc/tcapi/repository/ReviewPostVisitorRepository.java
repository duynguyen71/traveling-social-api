package com.tc.tcapi.repository;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostVisitor;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPostVisitorRepository extends JpaRepository<ReviewPostVisitor, Long> {

    List<ReviewPostVisitor> getByReviewPost(ReviewPost reviewPost);

    int countByReviewPost(ReviewPost reviewPost);
    Optional<ReviewPostVisitor> findByReviewPost_IdAndUser_IdAndStatus(Long reviewPost, Long user,Integer status);

    Optional<ReviewPostVisitor> findByReviewPostAndUser(ReviewPost reviewPost, User user);


    boolean existsByUser_IdAndReviewPost_IdAndStatus(Long userId, Long reviewPostId, Integer status);

}
