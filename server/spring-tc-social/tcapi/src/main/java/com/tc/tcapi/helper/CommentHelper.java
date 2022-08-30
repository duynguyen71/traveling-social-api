package com.tc.tcapi.helper;

import com.tc.tcapi.model.FileUpload;
import com.tc.tcapi.model.Post;
import com.tc.tcapi.model.PostComment;
import com.tc.tcapi.model.User;
import com.tc.core.request.PostCommentRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.CommentResponse;
import com.tc.core.response.UserInfoResponse;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.FileStorageService;
import com.tc.tcapi.service.PostCommentService;
import com.tc.tcapi.service.PostService;
import com.tc.tcapi.service.UserService;
import com.tc.tcapi.utilities.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentHelper {

    private final UserService userService;
    private final PostService postService;
    private final PostCommentService commentService;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    private final NotificationUtil notificationUtil;

    public ResponseEntity<?> getRootComments(Long postId, Map<String, String> params) {
        BaseParamRequest paramRequest = new BaseParamRequest(params);
        Pageable pageable = paramRequest.toPageRequest();
        List<PostComment> postComments = commentService.getPostParentComments(postId, 1, pageable);
        List<CommentResponse> rs = postComments.stream()
                .map(comment -> {
                    CommentResponse commentResp = modelMapper.map(comment, CommentResponse.class);
                    commentResp.setReplyCount(commentService.countReply(comment));
                    commentResp.setUser(modelMapper.map(comment.getUser(), UserInfoResponse.class));
                    return commentResp;
                }).collect(Collectors.toList());
        return BaseResponse.success(rs, "Get post comments success!");

    }

    public ResponseEntity<?> commentPost(Long id, PostCommentRequest request) {
        Post post = postService.getById(id);
        if (post != null) {
            Long attachmentId = request.getAttachmentId();
            String content = request.getContent();
            if (attachmentId == null && content == null)
                return BaseResponse.badRequest("Comment content is empty");
            Long commentReqId = request.getId();
            PostComment postComment;
            User currentUser = userService.getCurrentUser();
            if (commentReqId != null
                    && (postComment = commentService.getByIdAndUser(commentReqId, currentUser)) != null) {
                //update comment
            } else {
                postComment = new PostComment();
                postComment.setPost(post);
                postComment.setUser(currentUser);
            }
            postComment.setContent(content);
            postComment.setStatus(1);
            //
            Long parentCommentId = request.getParentCommentId();
            PostComment parentComment = null;
            if (parentCommentId != null && (parentComment = commentService.getById(parentCommentId)) != null)
                postComment.setParent(parentComment);
            //attachment
            if (attachmentId != null) {
                //TODO: save attachment
                FileUpload fileUpload = fileStorageService.getFileFromDb(attachmentId);
                if (fileUpload != null)
                    postComment.setAttachment(fileUpload);
            }
            postComment = commentService.save(postComment);
            CommentResponse commentResponse = modelMapper.map(postComment, CommentResponse.class);
            commentResponse.setUser(modelMapper.map(postComment.getUser(), UserInfoResponse.class));
            notificationUtil.sendCommentNotification(commentResponse.getContent(), post.getUser());
            return BaseResponse.success(commentResponse, "Post comment success");
        }
        return BaseResponse.badRequest("Can not find post with id: " + id);
    }

    public ResponseEntity<?> getReplyComments(Long commentId, Map<String, String> params) {
        PostComment parent = commentService.getByIdAndStatus(commentId, 1);
        List<PostComment> comments = commentService.getReplyComments(parent);
        UserInfoResponse replyUser = modelMapper.map(parent.getUser(), UserInfoResponse.class);

        List<CommentResponse> rs = comments.stream()
                .map(comment -> {
                    CommentResponse commentResp = modelMapper.map(comment, CommentResponse.class);
                    commentResp.setReplyCount(commentService.countReply(comment));
                    commentResp.setUser(modelMapper.map(comment.getUser(), UserInfoResponse.class));
                    commentResp.setReplyUser(replyUser);
                    return commentResp;
                }).collect(Collectors.toList());
        return BaseResponse.success(rs, "get reply comments success");
    }

    public ResponseEntity<?> getCurrentUserPostComments(Long postId) {
        User currentUser = userService.getCurrentUser();
        List<PostComment> comments = commentService.getCurrentUserPostComments(postService.getById(postId), currentUser, 1);
        List<CommentResponse> rs = comments.stream()
                .map(c -> {
                    CommentResponse commentResponse = modelMapper.map(c, CommentResponse.class);
                    commentResponse.setReplyCount(commentService.countReply(c));
                    return commentResponse;
                })
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get current user post comments success!");
    }

    public ResponseEntity<?> updateCommentStatus(Long commentId, Integer status) {
        User user = userService.getCurrentUser();
        PostComment comment = commentService.getByIdAndUser(commentId, user);
        if (comment != null) {
            comment.setStatus(status);
            commentService.save(comment);
            return BaseResponse.success("update comment status success");
        }
        return BaseResponse.badRequest("Could not find comment with id: " + commentId);
    }
}
