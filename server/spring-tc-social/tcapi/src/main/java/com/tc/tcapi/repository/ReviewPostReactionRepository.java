package com.tc.tcapi.repository;

import com.tc.core.model.ReviewPost;
import com.tc.core.model.ReviewPostReaction;
import com.tc.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPostReactionRepository extends JpaRepository<ReviewPostReaction,Long> {

    Optional<ReviewPostReaction> findByReviewPostAndUser(ReviewPost reviewPost, User user);

    Optional<ReviewPostReaction> findByReviewPostAndUserAndStatus(ReviewPost reviewPost, User user,int status);

    List<ReviewPostReaction> findByReviewPostAndStatus(ReviewPost reviewPost, int status);

    int countByReviewPostAndStatus(ReviewPost reviewPost,int status);



}
