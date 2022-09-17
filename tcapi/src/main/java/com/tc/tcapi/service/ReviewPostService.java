package com.tc.tcapi.service;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.Tag;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.ReviewPostRepository;
import com.tc.tcapi.request.BaseParamRequest;
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

    public List<ReviewPost> getReviewPosts(Integer status, BaseParamRequest baseParamRequest) {
        return repo.getPopularReviewPostsNative(4,4*baseParamRequest.getPage());
    }
  public List<ReviewPost> getNewestReviewPost(Pageable pageable) {
        return repo.findByStatus(1,pageable);
    }

    public Double countRating(Long reviewPostId){
        return repo.countRating(reviewPostId);
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

    public List<ReviewPost> searchReviewPostsNative(String keyword,Pageable pageable){
        return repo.searchReviewPostsNative(keyword,pageable);
    }

    public List<ReviewPost> getBookmarks(User user,int status,Pageable pageable) {
        return repo.findByVisitors_UserAndVisitors_Status(user,status,pageable);
    }
}
