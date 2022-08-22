package com.tc.tcapi.service;

import com.tc.core.model.Post;
import com.tc.core.model.PostComment;
import com.tc.core.model.User;
import com.tc.tcapi.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommentService {

    private final PostCommentRepository commentRepo;

    public PostComment getById(Long id) {
        return commentRepo.findById(id).orElse(null);
    }

    public PostComment getByIdAndStatus(Long id, Integer status){
        return commentRepo.findByIdAndStatus(id,status).orElse(null);
    }

    public PostComment getByIdAndUser(Long id, User user) {
        return commentRepo.findByIdAndUser(id, user).orElse(null);
    }

    public PostComment save(PostComment postComment) {
        return commentRepo.saveAndFlush(postComment);
    }

    /**
     * get post parent comments
     *
     * @param postId
     * @param status
     * @param pageable
     * @return
     */
    public List<PostComment> getPostParentComments(Long postId, Integer status, Pageable pageable) {
        return commentRepo.getPostCommentsNative(postId, status, pageable);
    }

    public List<PostComment> getCurrentUserPostComments(Post post, User user, Integer status) {
        return commentRepo.findByPostAndUserAndStatusAndParentIsNull(post, user, status);
    }

    public List<PostComment> getReplyComments(PostComment comment){
        return commentRepo.getByParentAndStatus(comment,1);
    }


    public int countAllComments(Post post) {
        return commentRepo.countByPostAndStatus(post, 1);
    }

    public Integer countReply(PostComment root) {
        return commentRepo.countByParentAndStatus(root, 1);
    }

}
