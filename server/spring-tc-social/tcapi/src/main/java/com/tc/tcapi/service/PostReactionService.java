package com.tc.tcapi.service;

import com.tc.core.model.Post;
import com.tc.core.model.PostReaction;
import com.tc.core.model.Reaction;
import com.tc.core.model.User;
import com.tc.tcapi.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionRepository postReactionRepo;

    public void save(PostReaction postReaction) {
        postReactionRepo.save(postReaction);
    }

    public PostReaction getByUser(User user, Post post, Integer status) {
        return postReactionRepo.findByUserAndPostAndStatus(user,post, status).orElse(null);
    }

    public List<PostReaction> getPostReactions(Post post, Integer status) {
        return postReactionRepo.getByPostAndStatus(post, status);
    }

    public boolean existByUserAndReaction(User user, Reaction reaction) {
        return postReactionRepo.existsByUserAndReaction(user, reaction);
    }

    public boolean existByUser(User user) {
        return postReactionRepo.existsByUser(user);
    }

    public Long countReactions(Post post) {
        return postReactionRepo.countByPostAndStatus(post,1);
    }

}
