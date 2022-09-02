package com.tc.tcapi.helper;

import com.tc.tcapi.model.*;
import com.tc.core.request.*;
import com.tc.core.response.*;
import com.tc.core.utilities.ValidationUtil;
import com.tc.tcapi.repository.ReviewPostVisitorService;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("ReviewHelper")
@RequiredArgsConstructor
@Slf4j
public class ReviewPostHelper {

    private final ReviewPostService reviewPostService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ReviewPostAttachmentService attService;
    private final TagService tagService;
    private final ReviewPostVisitorService postVisitorService;
    private final ReviewPostReactionService reactionService;
    private final ReviewPostCommentService commentService;
    private final PostService postService;
    private final FollowService followService;

    public ResponseEntity<?> getReviewPosts(Map<String, String> param) {

        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageable = baseParamRequest.toPageRequest();

        List<ReviewPost> reviews = reviewPostService.getReviewPosts(1, pageable);
        List<BaseReviewPostResponse> rs = reviews.stream()
                .map(r -> modelMapper.map(r, BaseReviewPostResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get review posts success!");
    }

    /**
     * Get review post detail
     */
    public ResponseEntity<?> getReviewPostDetail(Long reviewId) {
        ReviewPost review = reviewPostService.getByIdAndStatus(reviewId, 1);
        if (review != null) {
            //save visitor
            User currentUser = userService.getCurrentUser();
            ReviewPostVisitor visitor = postVisitorService.getByUserAndReviewPost(currentUser, review);
            if (visitor == null) {
                ReviewPostVisitor reviewPostVisitor = new ReviewPostVisitor();
                reviewPostVisitor.setUser(currentUser);
                reviewPostVisitor.setReviewPost(review);
                reviewPostVisitor.setStatus(0);
                postVisitorService.save(reviewPostVisitor);
            }
            // get review post detail
            ReviewDetailResponse rs = modelMapper.map(review, ReviewDetailResponse.class);
            // count viewer
            int countVisitor = postVisitorService.countVisitor(review);
            rs.setNumOfVisitor(countVisitor);
            // count reaction
            int countReaction = reactionService.countAllActiveReaction(review);
            rs.setNumOfReaction(countReaction);
            // count comment
            rs.setNumOfComment(commentService.countAllActiveComments(review));
            //get current user reaction
            ReviewPostReaction userReaction = reactionService.getUserReaction(review, currentUser, 1);
            if (userReaction != null) {
                rs.setReaction(modelMapper.map(userReaction, ReactionResponse.class));
            }
            // check is bookmark
            boolean bookmark = postVisitorService.hasBookmark(currentUser.getId(), reviewId);
            rs.setHasBookmark(bookmark);
            // get attachments
            List<ReviewPostImage> images = attService.getImages(review, 1);
            rs.setImages(images.stream()
                    .map(i -> modelMapper.map(i, ReviewPostAttachmentResponse.class)).collect(Collectors.toSet()));
            return BaseResponse.success(rs, "Get review post detail with id: " + reviewId + " success!");
        }
        return BaseResponse.badRequest("Can not find review post with id: " + reviewId);
    }

    // get current user review-posts
    public ResponseEntity<?> getCurrentUserReviewPosts(Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<ReviewPost> reviews = reviewPostService.getUserReviewPosts(currentUser, 1, baseParamRequest.toPageRequest());
        List<ReviewPostReport> rs = reviews.stream()
                .map(r -> {
                    ReviewPostReport reviewPostReport = modelMapper.map(r, ReviewPostReport.class);
                    reviewPostReport.setNumOfComment(commentService.countAllActiveComments(r));
                    reviewPostReport.setNumOfVisitor(postVisitorService.countVisitor(r));
                    reviewPostReport.setNumOfLike(reactionService.countAllActiveReaction(r));
                    return reviewPostReport;
                }).collect(Collectors.toList());
        return BaseResponse.success(rs, "get current user review posts success");
    }

    public ResponseEntity<?> getUserReviewPosts(Long userId, Map<String, String> param) {
        User currentUser = userService.getById(userId);
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        List<ReviewPost> reviews = reviewPostService.getUserReviewPosts(currentUser, 1, baseParamRequest.toPageRequest());
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
        if (id == null || (reviewPost = reviewPostService.getByIdAndUser(id, currentUser)) == null)
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
        reviewPost.setCoverPhoto(coverImage);
        reviewPost = reviewPostService.saveFlush(reviewPost);
        // Save tags
        Set<TagRequest> tags = request.getTags();
        for (TagRequest tagReq :
                tags) {
            Tag tag;
            String tagReqName = tagReq.getName();
            tag = tagService.getByName(tagReqName);
            if (tag == null) {
                tag = tagService.saveFlush(Tag.builder().name(tagReqName).build());
            }
            if (tagReq.getStatus() == 0) {
                reviewPost.getTags().remove(tag);
            } else {
                reviewPost.getTags().add(tag);
            }
            reviewPostService.save(reviewPost);
        }
        // Save photo
        for (ReviewPostAttachmentRequest attachmentRequest : attachments) {
            Long attachmentRequestId = attachmentRequest.getId();
            ReviewPostImage attachment;
            if (attachmentRequestId == null || (attachment = attService.getAttachment(attachmentRequestId)) == null)
                attachment = new ReviewPostImage();
            attachment.setStatus(attachmentRequest.getStatus());
            attachment.setPos(attachmentRequest.getPos());
            // set attachment photo
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

    /**
     * search by title, tagName
     */
    public ResponseEntity<?> searchReviewPosts(Map<String, String> param) {
        BaseParamRequest paramRequest = new BaseParamRequest(param);
        String keyWord = null;
        if (!ValidationUtil.isNullOrBlank(param.get("keyWord"))) {
            keyWord = param.get("keyWord");
        }
        Pageable pageable = paramRequest.toPageRequest();
        List<ReviewPost> posts = reviewPostService.searchPosts("%" + keyWord + "%", "%" + keyWord + "%", pageable);
        List<BaseReviewPostResponse> data = posts.stream()
                .map(reviewPost ->
                        modelMapper.map(reviewPost, BaseReviewPostResponse.class))
                .toList();
        return BaseResponse.success(data, "search review posts by keyword " + keyWord + " success");
    }


    public ResponseEntity<?> getBookmarkReviewPosts(Map<String, String> param) {
        Pageable pageable = new BaseParamRequest(param).toNativePageRequest("createDate");
        User user = userService.getCurrentUser();
        List<ReviewPost> bookmarks = reviewPostService.getBookmarks(user, 1, pageable);
        log.info("book mark length: {}",bookmarks.size());
        List<BaseReviewPostResponse> data = bookmarks.stream()
                .map(reviewPost -> modelMapper.map(reviewPost, BaseReviewPostResponse.class))
                .toList();
        return BaseResponse.success(data, "Get bookmarks success!");
    }

    public ResponseEntity<?> bookmarkReviewPost(Long id) {
        ReviewPostVisitor visit =
                postVisitorService.getByUserAndReviewPost(userService.getCurrentUser(), reviewPostService.getById(id));
        if (visit == null) {
            visit = new ReviewPostVisitor();
            visit.setUser(userService.getCurrentUser());
            visit.setReviewPost(reviewPostService.getById(id));
        }
        visit.setStatus(1);
        postVisitorService.save(visit);
        return BaseResponse.success("Bookmark review post " + id + " success!");
    }

    public ResponseEntity<?> getAuth(Long reviewId) {
        User user = userService.getReviewPostAuth(reviewId);
        if (user == null) return BaseResponse.badRequest("Can not find auth");
        ReviewPostAuthResponse data = modelMapper.map(user, ReviewPostAuthResponse.class);
        data.setNumOfReviewPost(reviewPostService.countActiveReviewPost(user));
        data.setNumOfPost(postService.countActivePost(user));
        data.setNumOfFollower(followService.countFollower(user, 1));
        data.setIsFollowing(followService.isFollowed(user, userService.getCurrentUser(), 1));
        return BaseResponse.success(data, "Get auth success!");

    }

    public ResponseEntity<?> getCurrentUserReviewPostDetail(Long id) {
        ReviewPost reviewPost = reviewPostService.getById(id);
        ReviewPostEditDetailResponse data = modelMapper.map(reviewPost, ReviewPostEditDetailResponse.class);
        List<ReviewPostImage> attachments = attService.getImages(reviewPost, 1);
        List<ReviewPostEditAttachmentResponse> attachmentResponses = new LinkedList<>();
        for (int i = 0; i < attachments.size(); i++) {
            ReviewPostImage image = attachments.get(i);
            ReviewPostEditAttachmentResponse attResponse = new ReviewPostEditAttachmentResponse();
            attResponse.setId(image.getId());
            attResponse.setImageId(image.getImage().getId());
            attResponse.setName(image.getImage().getName());
            attResponse.setStatus(1);
            attResponse.setPos(image.getStatus());
            attachmentResponses.add(attResponse);
        }
        data.setImages(attachmentResponses);
        return BaseResponse.success(data, "get current user review post detail success!");
    }

    public ResponseEntity<?> removeBookmark(Long id) {
        ReviewPostVisitor bookmarkedReviewPost = postVisitorService.getBookmarkedReviewPost(id, userService.getCurrentUser().getId());
        if (bookmarkedReviewPost == null)
            return BaseResponse.badRequest("Bookmark review post id: " + id + "was not existed!");
        bookmarkedReviewPost.setStatus(0);
        postVisitorService.save(bookmarkedReviewPost);
        return BaseResponse.success("Remove bookmark post: " + id + " success!");
    }
}
