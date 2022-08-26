package com.tc.tcapi.service;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostReaction;
import com.tc.tcapi.model.User;
import com.tc.core.request.ReactionRequest;
import com.tc.tcapi.repository.ReviewPostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewPostReactionService {

    private final ReviewPostReactionRepository repo;

    public int countAllActiveReaction(ReviewPost reviewPost) {
        return repo.countByReviewPostAndStatus(reviewPost, 1);
    }

    public ReviewPostReaction getUserReaction(ReviewPost reviewPost, User user) {
        return repo.findByReviewPostAndUser(reviewPost, user).orElse(null);
    }

    public ReviewPostReaction getUserReaction(ReviewPost reviewPost, User user, int status) {
        return repo.findByReviewPostAndUserAndStatus(reviewPost, user, status).orElse(null);
    }

    public List<ReviewPostReaction> getUserReaction(ReviewPost reviewPost) {
        return repo.findByReviewPostAndStatus(reviewPost, 1);
    }

    public ResponseEntity<?> reactionPost(ReactionRequest request) {

        return null;
    }

    public void save(ReviewPostReaction reviewPostReaction) {
        repo.save(reviewPostReaction);
    }
}
