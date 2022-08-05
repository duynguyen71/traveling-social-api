package com.tc.tcapi.helper;

import com.tc.core.model.FileUpload;
import com.tc.core.model.ReviewPost;
import com.tc.core.model.ReviewPostImage;
import com.tc.core.model.User;
import com.tc.core.request.BaseParamRequest;
import com.tc.core.request.CreateReviewPostRequest;
import com.tc.core.request.ReviewPostAttachmentRequest;
import com.tc.core.request.ReviewRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.BaseReviewPostResponse;
import com.tc.core.response.ReviewDetailResponse;
import com.tc.core.response.ReviewPostResponse;
import com.tc.tcapi.service.FileStorageService;
import com.tc.tcapi.service.ReviewPostAttachmentService;
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
public class ReviewPostHelper {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ReviewPostAttachmentService attService;

    public ResponseEntity<?> saveReview(ReviewRequest request) {
        User user = userService.getCurrentUser();
        Long reqId = request.getId();
        ReviewPost review;
        if (reqId != null && (review = reviewService.getByIdAndUser(reqId, user)) != null) {

        } else {
            review = new ReviewPost();
            review.setUser(user);
            review.setStatus(1);
        }
        review.setTitle(request.getTitle());
        review.setCost(request.getCost());

        review.setTotalDay(request.getTotalDay());
        review.setNumOfParticipant(request.getTotalMember());

        FileUpload coverPhoto = fileStorageService.getById(request.getCoverPhoto());
        review.setCoverImage(coverPhoto);

        for (Long id :
                request.getPhotos()) {
            FileUpload p = fileStorageService.getById(id);
//            review.getImages().add(p);
        }
        review = reviewService.saveReview(review);

        ReviewDetailResponse rs = modelMapper.map(review, ReviewDetailResponse.class);

        return BaseResponse.success(rs, "save review post success");
    }

    public ResponseEntity<?> getReviewPosts(Map<String, String> param) {

        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageable = baseParamRequest.toPageRequest();

        List<ReviewPost> reviews = reviewService.getReviewPosts(1, pageable);
        List<BaseReviewPostResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, BaseReviewPostResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get review posts success!");
    }

    public ResponseEntity<?> getReviewPostDetail(Long reviewId) {
        ReviewPost review = reviewService.getByIdAndStatus(reviewId, 1);
        if (review != null) {
            ReviewDetailResponse rs = modelMapper.map(review, ReviewDetailResponse.class);

            return BaseResponse.success(rs, "Get review post detail with id: " + reviewId + " success!");
        }
        return BaseResponse.badRequest("Can not find review post with id: " + reviewId);
    }

    public ResponseEntity<?> getCurrentUserReviewPosts(Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<ReviewPost> reviews = reviewService.getUserReviewPosts(currentUser, baseParamRequest.getStatus(), baseParamRequest.toPageRequest());
        List<ReviewPostResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, ReviewPostResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get current user review posts success");
    }

    public ResponseEntity<?> getUserReviewPosts(Long userId, Map<String, String> param) {
        User currentUser = userService.getById(userId);
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<ReviewPost> reviews = reviewService.getUserReviewPosts(currentUser, 1, baseParamRequest.toPageRequest());
        List<ReviewPostResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, ReviewPostResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get user review posts success");
    }


    public ResponseEntity<?> createReviewPost(CreateReviewPostRequest request) {
        Long id = request.getId();
        List<ReviewPostAttachmentRequest> attachments = request.getImages();
        String title = request.getTitle();
        String content = request.getContent();
        Long coverImageId = request.getCoverImageId();
        String contentJson = request.getContentJson();
        ReviewPost reviewPost;
        User currentUser = userService.getCurrentUser();
        if (id == null || (reviewPost = reviewService.getByIdAndUser(id, currentUser)) == null)
            reviewPost = new ReviewPost();
        reviewPost.setTitle(title);
        reviewPost.setContent(content);
        reviewPost.setContentJson(contentJson);
        reviewPost.setStatus(1);
        reviewPost.setCost(request.getCost());
        reviewPost.setNumOfParticipant(request.getNumOfParticipant());
        reviewPost.setTotalDay(request.getTotalDay());
        reviewPost.setUser(currentUser);
        FileUpload coverImage = fileStorageService.getById(coverImageId);
        if (coverImage == null) {
            return BaseResponse.badRequest("Create review post failed: cover image not found");
        }
        reviewPost.setCoverImage(coverImage);
        reviewPost = reviewService.saveFlush(reviewPost);

        // Save photo
        for (ReviewPostAttachmentRequest attachmentRequest : attachments) {
            Long attachmentRequestId = attachmentRequest.getId();
            ReviewPostImage attachment;
            if (attachmentRequestId == null || (attachment = attService.getAttachment(attachmentRequestId)) == null)
                attachment = new ReviewPostImage();
            attachment.setStatus(attachmentRequest.getStatus());
            attachment.setPos(attachmentRequest.getPos());
            Long photoRequestId = attachmentRequest.getImageId();
            FileUpload photo = fileStorageService.getById(photoRequestId);
            if (photoRequestId == null || photo == null) {
                continue;
            }
            attachment.setImage(photo);
            attachment.setReviewPost(reviewPost);
            attService.save(attachment);
        }
        log.info("Create review post success id:{}", reviewPost.getId());
        return BaseResponse.success(modelMapper.map(reviewPost, ReviewPostResponse.class), "Create review post success!");

    }
}
