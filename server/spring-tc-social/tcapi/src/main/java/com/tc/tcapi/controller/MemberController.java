package com.tc.tcapi.controller;

import com.tc.tcapi.helper.*;
import com.tc.core.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserHelper userHelper;
    private final PostHelper postHelper;
    private final FileUploadHelper fileUploadHelper;
    private final CommentHelper commentHelper;
    private final PostReactionHelper postReactionHelper;
    private final ReviewHelper reviewHelper;
    private final ChatGroupHelper chatGroupHelper;
    private final MessageHelper messageHelper;
    private final UserNotificationHelper userNotificationHelper;
    private final ChatGroupUserHelper chatgroupUserHelper;

    @PutMapping("/users/me/using-app/{status}")
    public ResponseEntity<?> setUsingApp(@PathVariable Integer status) {
        return userHelper.setUsingApp(status);
    }

    @GetMapping("/users/me/files/{name}")
    public ResponseEntity<?> findFile(@PathVariable("name") String fileName) {
        return userHelper.getImage(fileName);
    }

    @PostMapping("/users/me/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadHelper.uploadFile(file);
    }

    @PostMapping("/users/me/files")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") List<MultipartFile> files) {
        return fileUploadHelper.uploadFiles(files);
    }

    @GetMapping("/users/searching")
    public ResponseEntity<?> search(@RequestParam Map<String, String> params) {
        return userHelper.searchingUsers(params);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId) {
        return userHelper.getUserProfile(userId);
    }

    @PutMapping("/users/me/avt")
    public ResponseEntity<?> uploadAvt(@RequestParam("file") MultipartFile file) {
        return userHelper.updateAvt(file);
    }

    @PutMapping("/users/me/background")
    public ResponseEntity<?> updateBackground(@RequestParam("file") MultipartFile file) {
        return userHelper.updateBackground(file);
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUserInfo() {
        return userHelper.getCurrentUserDetail();
    }

    /**
     * Get current user notifications
     */
    @GetMapping("/users/me/notifications")
    public ResponseEntity<?> getCurrentUserNotification() {
        return userNotificationHelper.getCurrentUserNotifications();
    }

    @PostMapping("/users/me/notifications")
    public ResponseEntity<?> saveNotification(@RequestBody @Valid NotificationRequest request) {
        return userNotificationHelper.saveNotification(request);
    }

    @PostMapping("/users/me")
    public ResponseEntity<?> updateCurrentUserInfo(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userHelper.updateCurrentUserInfo(userUpdateRequest);
    }

    @GetMapping("/users/me/posts")
    public ResponseEntity<?> getCurrentUserPosts(@RequestParam Map<String, String> params) {
        return postHelper.getCurrentUserPosts(params);
    }

    @PutMapping("/posts/{postId}/status/{status}")
    public ResponseEntity<?> updatePostStatus(@PathVariable("postId") Long postId, @PathVariable("status") Integer status) {
        return postHelper.updateStatus(postId, status);
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<?> getPosts(@RequestParam Map<String, String> params) {
        return postHelper.getPosts(params);
    }

    @GetMapping("/users/me/posts/{postId}/comments")
    public ResponseEntity<?> getPostParentComments(@PathVariable("postId") Long postId) {
        return postHelper.getPostComments(postId);
    }

    @PostMapping("/users/me/posts")
    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostRequest createPostRequest) {
        return postHelper.createPost(createPostRequest);
    }

    @PostMapping("/review-post")
    public ResponseEntity<?> createReviewPost(@RequestBody ReviewPostRequest reviewPostRequest,
                                              @RequestParam Long coverImageId,
                                              @RequestParam Long[] reviewPostImageIds) {
        return postHelper.createReviewPost(reviewPostRequest, coverImageId, reviewPostImageIds);
    }


    @GetMapping("/users/me/following")
    public ResponseEntity<?> getFollowingUsers(@RequestParam Map<String, String> param) {
        return userHelper.getFollowingUsers(param);
    }

    @GetMapping("/users/me/follow/{userId}")
    public ResponseEntity<?> followRequest(@PathVariable("userId") Long userId) {
        return userHelper.followUser(userId);
    }

    @GetMapping("/users/me/followers")
    public ResponseEntity<?> getFollowers(@RequestParam Map<String, String> param) {
        return userHelper.getFollowers(param);
    }

    @GetMapping("/users/me/unfollow/{id}")
    public ResponseEntity<?> unFollow(@PathVariable("id") Long id) {
        return userHelper.unFollowUser(id);
    }


    @GetMapping("/stories")
    public ResponseEntity<?> getFriendStories(@RequestParam Map<String, String> params) {
        return postHelper.getStories(params);
    }

    @GetMapping("/users/me/stories")
    public ResponseEntity<?> getCurrentUserStories(@RequestParam Map<String, String> params) {
        return postHelper.getStories(params);
    }


    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> commentPost(@PathVariable("id") Long id, @RequestBody PostCommentRequest postCommentRequest) {
        return commentHelper.commentPost(id, postCommentRequest);
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<?> getPostParentComments(@PathVariable("id") Long postId, @RequestParam Map<String, String> params) {
        return commentHelper.getRootComments(postId, params);
    }

    @PutMapping("/comments/{commentId}/status/{status}")
    public ResponseEntity<?> updateCommentStatus(@PathVariable("commentId") Long commentId, @PathVariable("status") Integer status) {
        return commentHelper.updateCommentStatus(commentId, status);
    }

    /**
     * get current user comments on post
     */
    @GetMapping("/posts/{postId}/comments/me")
    public ResponseEntity<?> getCurrentUserPostComments(@PathVariable("postId") Long postId) {
        return commentHelper.getCurrentUserPostComments(postId);
    }

    @GetMapping("/comments/{commentId}/reply")
    public ResponseEntity<?> getReplyComments(@PathVariable("commentId") Long commentId, @RequestParam Map<String, String> params) {
        return commentHelper.getReplyComments(commentId, params);
    }

    /**
     * drop reaction (post,story)
     */
    @PostMapping("/posts/reactions")
    public ResponseEntity<?> postReaction(@RequestBody @Valid ReactionRequest reactionRequest) {
        return postReactionHelper.savePostReaction(reactionRequest);
    }

    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<?> getPostReactions(@PathVariable("postId") Long postId) {
        return postReactionHelper.getPostReactions(postId);
    }

    //get current user review posts
    @GetMapping("/users/me/reviews")
    public ResponseEntity<?> getCurrentUserReviewPosts(@RequestParam Map<String, String> param) {
        return reviewHelper.getCurrentUserReviewPosts(param);
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<?> getCurrentUserReviewPosts(@RequestParam Map<String, String> param, @PathVariable("userId") Long userId) {
        return reviewHelper.getUserReviewPosts(userId, param);
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewPosts(@RequestParam Map<String, String> param) {
        return reviewHelper.getReviewPosts(param);
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> saveReview(@RequestBody @Valid ReviewRequest request) {
        return reviewHelper.saveReview(request);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> getReviewPostDetail(@PathVariable("reviewId") Long reviewId) {
        return reviewHelper.getReviewPostDetail(reviewId);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getTopActiveUsers(@RequestParam Map<String, String> param) {
        return userHelper.getTopUsers(param);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable("userId") Long userId, @RequestParam Map<String, String> param) {
        return postHelper.getUserPosts(userId, param);
    }

    /**
     * get current user chat groups
     */
    @GetMapping("/users/me/chat-groups")
    public ResponseEntity<?> getChatGroups(@RequestParam Map<String, String> param) {
        return chatGroupHelper.getChatGroups(param);
    }

    /**
     * Remove message using message id
     */
    @PutMapping("/users/me/messages/{messageId}")
    public ResponseEntity<?> removeMessage(@PathVariable("messageId") Long messageId) {
        return messageHelper.removeMessage(messageId);
    }

    /**
     * Create chat group
     */
    @PostMapping("/users/me/chat-groups")
    public ResponseEntity<?> createChatGroup(@RequestBody @Valid ChatGroupCreateRequest request) {
        return chatGroupHelper.createChatGroup(request);
    }

    /**
     * get chat group detail
     */
    @GetMapping("/users/me/chat-groups/{groupId}")
    public ResponseEntity<?> getChatGroupDetail(@PathVariable("groupId") Long groupId) {
        return chatGroupHelper.getChatGroupDetail(groupId);
    }

//    /**
//     * get chat group user detail
//     */
//    @GetMapping("/users/me/chat-groups/users/{id}")
//    public ResponseEntity<?> getChatGroupUserDetail(@PathVariable("id") Long chatGroupUserId){
//        return chatgroupUserHelper.getChatGroupUserDetail(chatGroupUserId);
//    }

    /**
     * Get group messages
     */
    @GetMapping("/chat-groups/{groupId}/messages")
    public ResponseEntity<?> getGroupMessages(@PathVariable("groupId") Long chatGroupId, @RequestParam Map<String, String> param) {
        return messageHelper.getMessages(chatGroupId, param);
    }

    /**
     * get chat group data between two user
     */
    @GetMapping("/users/me/chat-groups/users/{userId}")
    public ResponseEntity<?> getChatGroupWithUser(@PathVariable("userId") Long userId) {
        return chatGroupHelper.getChatGroupBetweenTwoUsers(userId);
    }

    /**
     * Save message
     */
    @PostMapping("/users/me/chat-groups/{groupId}/messages")
    public ResponseEntity<?> saveMessage(@PathVariable("groupId") Long chatGroupId, @RequestBody @Valid MessageRequest request) {
        return messageHelper.saveMessage(chatGroupId, request);
    }


}