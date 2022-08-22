package com.tc.tcapi.repository;

import com.tc.core.model.ReviewPost;
import com.tc.core.model.ReviewPostVisitor;
import com.tc.core.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewPostVisitorService {

    private final ReviewPostVisitorRepository repo;

    public void save(ReviewPostVisitor reviewPostVisitor) {
        repo.save(reviewPostVisitor);
    }

    public ReviewPostVisitor getByUserAndReviewPost(User user, ReviewPost review) {
        return repo.findByReviewPostAndUser(review, user).orElse(null);
    }

    public  ReviewPostVisitor getBookmarkedReviewPost(Long reviewPostId,Long userId){
        return repo.findByReviewPost_IdAndUser_IdAndStatus(reviewPostId,userId,1).orElse(null);
    }

    public int countVisitor(ReviewPost review) {
        return repo.countByReviewPost(review);
    }

    public boolean hasBookmark(Long userId, Long postId) {
        //status 1: bookmark
        return repo.existsByUser_IdAndReviewPost_IdAndStatus(userId, postId, 1);
    }
}
