package com.tc.tcapi.service;

import com.tc.core.model.Post;
import com.tc.core.model.ReviewPost;
import com.tc.core.model.ReviewPostComment;
import com.tc.core.model.User;
import com.tc.tcapi.repository.ReviewPostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewPostCommentService {

    private final ReviewPostCommentRepository commentRepo;


    public ReviewPostComment getById(Long id) {
        return commentRepo.findById(id).orElse(null);
    }

    public ReviewPostComment getByIdAndStatus(Long id, Integer status) {
        return commentRepo.findByIdAndStatus(id, status).orElse(null);
    }

    public ReviewPostComment getByIdAndUser(Long id, User user) {
        return commentRepo.findByIdAndUser(id, user).orElse(null);
    }

    public ReviewPostComment save(ReviewPostComment postComment) {
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
    public List<ReviewPostComment> getPostParentComments(Long postId, Integer status, Pageable pageable) {
        return commentRepo.getPostCommentsNative(postId, status, pageable);
    }

    public List<ReviewPostComment> getCurrentUserPostComments(ReviewPost post, User user, Integer status) {
        return commentRepo.findByPostAndUserAndStatusAndParentIsNull(post, user, status);
    }

    public List<ReviewPostComment> getReplyComments(ReviewPostComment comment) {
        return commentRepo.getByParentAndStatus(comment, 1);
    }

    public int  countAllActiveComments(ReviewPost post) {
        return commentRepo.countByPostAndStatus(post, 1);
    }

    public Integer countReply(ReviewPostComment root) {
        return commentRepo.countByParentAndStatus(root, 1);
    }

}
