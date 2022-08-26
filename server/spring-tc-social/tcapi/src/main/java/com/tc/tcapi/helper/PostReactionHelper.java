package com.tc.tcapi.helper;

import com.tc.tcapi.model.Post;
import com.tc.tcapi.model.PostReaction;
import com.tc.tcapi.model.Reaction;
import com.tc.tcapi.model.User;
import com.tc.core.request.ReactionRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.PostReactionResponse;
import com.tc.tcapi.service.PostReactionService;
import com.tc.tcapi.service.PostService;
import com.tc.tcapi.service.ReactionService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("PostReactionHelper")
@RequiredArgsConstructor
public class PostReactionHelper {

    private final PostReactionService postReactionService;
    private final PostService postService;
    private final ReactionService reactionService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> savePostReaction(ReactionRequest request) {
        User currentUser = userService.getCurrentUser();
        // check post id is valid
        Long postId = request.getPostId();
        Post post = postService.getById(postId);
        if (post == null)
            return BaseResponse.badRequest("Post is not found");
        // get post reaction
        PostReaction postReaction = postReactionService.getByUser(currentUser, post,1);
        if (postReaction == null)
            postReaction = new PostReaction();
        //
        Long reactionId = request.getReactionId();
        if (reactionId == null && postReaction != null) {
            postReaction.setStatus(0);
            postReactionService.save(postReaction);
            return BaseResponse.success(null, "Remove reaction success");
        }
        Reaction reaction = reactionService.getById(reactionId);
        if (reaction == null) {
            return BaseResponse.badRequest("Reaction id is invalid");
        }
        postReaction.setUser(currentUser);
        postReaction.setPost(post);
        postReaction.setReaction(reaction);
        postReaction.setStatus(1);

        postReactionService.save(postReaction);
        return BaseResponse.success(null, "save reaction success");
    }


    public ResponseEntity<?> getPostReactions(Long postId) {
        Post post = postService.getById(postId);
        List<PostReaction> postReactions = postReactionService.getPostReactions(post, 1);
        List<PostReactionResponse> rs = postReactions.stream()
                .map(r -> modelMapper.map(r, PostReactionResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, null);
    }
}
