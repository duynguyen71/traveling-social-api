package com.tc.tcapi.repository;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostVisitor;
import com.tc.tcapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public double getMyRating(Long reviewPostId,Long userId) {
        Optional<ReviewPostVisitor> optional = repo.findByUser_IdAndReviewPost_Id(userId, reviewPostId);
        if(optional.isEmpty())
        return 0;
        return optional.get().getRating();
    }
}
