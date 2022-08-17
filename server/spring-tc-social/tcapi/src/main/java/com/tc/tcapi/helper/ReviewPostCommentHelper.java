package com.tc.tcapi.helper;

import com.tc.core.model.*;
import com.tc.core.request.BaseParamRequest;
import com.tc.core.request.PostCommentRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.CommentResponse;
import com.tc.core.response.UserInfoResponse;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewPostCommentHelper {

    private final ReviewPostCommentService reviewPostCommentService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    private final ReviewPostService postService;

    public ResponseEntity<?> getRootComments(Long postId, Map<String, String> params) {
        log.info("get review post comments");
        BaseParamRequest paramRequest = new BaseParamRequest(params);
        Pageable pageable = paramRequest.toPageRequest();
        List<ReviewPostComment> postComments = reviewPostCommentService.getPostParentComments(postId, 1, pageable);
        log.info(postComments.toString());
        List<CommentResponse> rs = postComments.stream()
                .map(comment -> {
                    CommentResponse commentResp = modelMapper.map(comment, CommentResponse.class);
                    commentResp.setReplyCount(reviewPostCommentService.countReply(comment));
                    commentResp.setUser(modelMapper.map(comment.getUser(), UserInfoResponse.class));
                    return commentResp;
                }).collect(Collectors.toList());
        log.info(rs.toString());
        return BaseResponse.success(rs, "Get post comments success!");

    }

    public ResponseEntity<?> commentPost(Long id, PostCommentRequest request) {
        ReviewPost post = postService.getByIdAndStatus(id, 1);
        if (post != null) {
            Long attachmentId = request.getAttachmentId();
            String content = request.getContent();
            if (attachmentId == null && content == null)
                return BaseResponse.badRequest("Comment content is empty");
            Long commentReqId = request.getId();
            ReviewPostComment postComment;
            User currentUser = userService.getCurrentUser();
            if (commentReqId != null
                    && (postComment = reviewPostCommentService.getByIdAndUser(commentReqId, currentUser)) != null) {
                //update comment
            } else {
                postComment = new ReviewPostComment();
                postComment.setPost(post);
                postComment.setUser(currentUser);
            }
            postComment.setContent(content);
            postComment.setStatus(1);
            //
            Long parentCommentId = request.getParentCommentId();
            ReviewPostComment parentComment = null;
            if (parentCommentId != null && (parentComment = reviewPostCommentService.getById(parentCommentId)) != null)
                postComment.setParent(parentComment);
            //attachment
            if (attachmentId != null) {
                //TODO: save attachment
                FileUpload fileUpload = fileStorageService.getFileFromDb(attachmentId);
                if (fileUpload != null)
                    postComment.setAttachment(fileUpload);
            }
            postComment = reviewPostCommentService.save(postComment);
            CommentResponse commentResponse = modelMapper.map(postComment, CommentResponse.class);
            commentResponse.setUser(modelMapper.map(postComment.getUser(), UserInfoResponse.class));
            return BaseResponse.success(commentResponse, "Post comment success");


        }
        return BaseResponse.badRequest("Can not find post with id: " + id);
    }

    public ResponseEntity<?> getReplyComments(Long commentId, Map<String, String> params) {
        ReviewPostComment parent = reviewPostCommentService.getByIdAndStatus(commentId, 1);
        List<ReviewPostComment> comments = reviewPostCommentService.getReplyComments(parent);
        UserInfoResponse replyUser = modelMapper.map(parent.getUser(), UserInfoResponse.class);
        List<CommentResponse> rs = comments.stream()
                .map(comment -> {
                    CommentResponse commentResp = modelMapper.map(comment, CommentResponse.class);
                    commentResp.setReplyCount(reviewPostCommentService.countReply(comment));
                    commentResp.setReplyUser(replyUser);
                    commentResp.setUser(modelMapper.map(comment.getUser(), UserInfoResponse.class));
                    return commentResp;
                }).collect(Collectors.toList());
        log.info(rs.toString());
        return BaseResponse.success(rs, "get reply comments success");
    }

    public ResponseEntity<?> updateCommentStatus(Long commentId, Integer status) {
        User user = userService.getCurrentUser();
        ReviewPostComment comment = reviewPostCommentService.getByIdAndUser(commentId, user);
        if (comment != null) {
            comment.setStatus(status);
            reviewPostCommentService.save(comment);
            return BaseResponse.success("update comment status success");
        }
        return BaseResponse.badRequest("Could not find comment with id: " + commentId);
    }
}
