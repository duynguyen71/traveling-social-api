package com.tc.tcapi.helper;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostReaction;
import com.tc.tcapi.model.User;
import com.tc.core.request.ReactionRequest;
import com.tc.core.response.BaseResponse;
import com.tc.tcapi.service.ReactionService;
import com.tc.tcapi.service.ReviewPostReactionService;
import com.tc.tcapi.service.ReviewPostService;
import com.tc.tcapi.service.UserService;
import com.tc.tcapi.utilities.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewPostReactionHelper {

    private final ReviewPostReactionService service;
    private final UserService userService;
    private final ReviewPostService reviewPostService;
    private final ReviewPostReactionService reviewPostReactionService;
    private final ReactionService reactionService;
    private final NotificationUtil notificationUtil;

    public ResponseEntity<?> reactionPost(ReactionRequest request) {
        Long reactionId = request.getReactionId();
        Long reviewPostId = request.getPostId();
        User user = userService.getCurrentUser();
        ReviewPost reviewPost = reviewPostService.getById(reviewPostId);
        if (reviewPost == null)
            return BaseResponse.badRequest("Can not find review post with id: " + reviewPostId);

        ReviewPostReaction reviewPostReaction = reviewPostReactionService.getUserReaction(reviewPost, user);
        if (reviewPostReaction != null && request.getReactionId() == null) {
            reviewPostReaction.setStatus(0);
            reviewPostReactionService.save(reviewPostReaction);
            return BaseResponse.success("Remove reaction on review post " + reviewPostId + " success");
        }
        if (reviewPostReaction == null)
            reviewPostReaction = new ReviewPostReaction();
        reviewPostReaction.setReaction(reactionService.getById(reactionId));
        reviewPostReaction.setReviewPost(reviewPost);
        reviewPostReaction.setStatus(1);
        reviewPostReaction.setUser(user);
        reviewPostReactionService.save(reviewPostReaction);
        // notification
        notificationUtil.sendReactionNotification(reviewPost.getTitle(), reviewPost.getUser());
        return BaseResponse.success("Reaction review post " + reviewPostId + " success!");
    }

}