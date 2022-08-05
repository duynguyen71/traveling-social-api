package com.tc.tcapi.service;

import com.tc.core.model.ReviewPost;
import com.tc.core.model.User;
import com.tc.tcapi.repository.ReviewPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewPostRepository repo;

    public ReviewPost saveReview(ReviewPost review) {
        return repo.saveAndFlush(review);
    }

    public List<ReviewPost> getReviewPosts(Integer status, Pageable pageable) {
        return repo.findReviewPostsNative(status, pageable);
    }


    public ReviewPost getByIdAndUser(Long reqId, User user) {
        return repo.findByIdAndUser(reqId, user).orElse(null);
    }

    public ReviewPost getByIdAndStatus(Long reviewId, int status) {
        return repo.findByIdAndStatus(reviewId, status).orElse(null);
    }

    public List<ReviewPost> getUserReviewPosts(User user,Integer status, Pageable pageRequest) {
        return repo.findByUserAndStatus(user,status, pageRequest);
    }

    public ReviewPost saveFlush(ReviewPost reviewPost) {
        return repo.saveAndFlush(reviewPost);
    }
}
