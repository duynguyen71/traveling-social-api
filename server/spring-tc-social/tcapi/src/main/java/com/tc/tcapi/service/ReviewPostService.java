package com.tc.tcapi.service;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.Tag;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.ReviewPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewPostService {

    private final ReviewPostRepository repo;

    public void save(ReviewPost review) {
         repo.save(review);
    }

    public List<ReviewPost> getReviewPosts(Integer status, Pageable pageable) {
        return repo.findReviewPostsNative(status, pageable);
    }
    public ReviewPost getById(Long postId) {
        return repo.findById(postId).orElse(null);
    }

    public ReviewPost getByIdAndUser(Long reqId, User user) {
        return repo.findByIdAndUser(reqId, user).orElse(null);
    }

    public ReviewPost getByIdAndStatus(Long reviewId, int status) {
        return repo.findByIdAndStatus(reviewId, status).orElse(null);
    }
    public List<ReviewPost> getUserReviewPosts(User user,Integer status, Pageable pageRequest) {
        return repo.findByUserAndStatus(user,status);
    }

    public int countActiveReviewPost(User user){
        return repo.countByUserAndStatus(user,1);
    }

    public ReviewPost saveFlush(ReviewPost reviewPost) {
        return repo.saveAndFlush(reviewPost);
    }

    public List<ReviewPost> getPostsByTag(Tag tag){
        return repo.findByTags(tag);
    }

    public List<ReviewPost> searchPosts(String tagName,String title,Pageable pageable){
        return repo.searchReviewPostsNative(tagName,title,pageable);
    }

    public List<ReviewPost> getBookmarks(User user,int status,Pageable pageable) {
        return repo.findByVisitors_UserAndVisitors_Status(user,status,pageable);
    }
}
