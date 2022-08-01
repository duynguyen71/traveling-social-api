package com.tc.tcapi.helper;

import com.tc.core.model.FileUpload;
import com.tc.core.model.Review;
import com.tc.core.model.User;
import com.tc.core.request.BaseParamRequest;
import com.tc.core.request.ReviewRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.ReviewDetailResponse;
import com.tc.core.response.ReviewResponse;
import com.tc.tcapi.service.FileStorageService;
import com.tc.tcapi.service.ReviewService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ReviewHelper")
@RequiredArgsConstructor
@Slf4j
public class ReviewHelper {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    public ResponseEntity<?> saveReview(ReviewRequest request) {
        User user = userService.getCurrentUser();
        Long reqId = request.getId();
        Review review;
        if (reqId != null && (review = reviewService.getByIdAndUser(reqId, user)) != null) {

        } else {
            review = new Review();
            review.setUser(user);
            review.setStatus(1);
        }
        review.setTitle(request.getTitle());
        review.setCost(request.getCost());
        review.setDetail(request.getDetail());
        review.setTotalDay(request.getTotalDay());
        review.setTotalMember(request.getTotalMember());

        FileUpload coverPhoto = fileStorageService.getById(request.getCoverPhoto());
        review.setCoverPhoto(coverPhoto);

        for (Long id :
                request.getPhotos()) {
            FileUpload p = fileStorageService.getById(id);
            p.getReviews().add(review);
            review.getPhotos().add(p);
        }
        review = reviewService.saveReview(review);

        ReviewDetailResponse rs = modelMapper.map(review, ReviewDetailResponse.class);

        return BaseResponse.success(rs, "save review post success");
    }

    public ResponseEntity<?> getReviewPosts(Map<String, String> param) {

        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageable = baseParamRequest.toPageRequest();

        List<Review> reviews = reviewService.getReviewPosts(1, pageable);
        List<ReviewResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, ReviewResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get review posts success");
    }

    public ResponseEntity<?> getReviewPostDetail(Long reviewId) {
        Review review = reviewService.getByIdAndStatus(reviewId, 1);
        if (review != null) {
            ReviewDetailResponse rs = modelMapper.map(review, ReviewDetailResponse.class);
            return BaseResponse.success(rs, "Get review post detail with id: " + reviewId + " success!");
        }
        return BaseResponse.badRequest("Can not find review post with id: " + reviewId);
    }

    public ResponseEntity<?> getCurrentUserReviewPosts(Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<Review> reviews = reviewService.getUserReviewPosts(currentUser, baseParamRequest.getStatus(), baseParamRequest.toPageRequest());
        List<ReviewResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, ReviewResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get current user review posts success");
    }

    public ResponseEntity<?> getUserReviewPosts(Long userId, Map<String, String> param) {
        User currentUser = userService.getById(userId);
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<Review> reviews = reviewService.getUserReviewPosts(currentUser, 1, baseParamRequest.toPageRequest());
        List<ReviewResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, ReviewResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get user review posts success");
    }


}
