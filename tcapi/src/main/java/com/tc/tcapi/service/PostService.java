package com.tc.tcapi.service;

import com.tc.core.enumm.EPostType;
import com.tc.tcapi.model.*;
import com.tc.tcapi.repository.PostCommentRepository;
import com.tc.tcapi.repository.PostContentRepository;
import com.tc.tcapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repo;
    private final PostContentRepository postMediaFileRepo;
    private final PostCommentRepository postCommentRepo;

    public List<Post> findAllPosts(EPostType type,int status,Pageable pageable){
        return repo.findAllByTypeAndStatus(type,status,pageable);
    };
    public List<Post> findPosts(int status, EPostType type){
        return repo.findByStatusAndTypeOrderByCreateDateDesc(status,type);
    };

    public List<Post> searchPosts(String tagName, String caption, int type, Pageable pageable) {
        return repo.searchPostsNative(tagName, caption, type, pageable);
    }

    public Post getCurrentUserPost(Long postId, User u) {
        return repo.findByIdAndUser(postId, u).orElse(null);
    }

    public Post savePostAndFlush(Post post) {
        return repo.saveAndFlush(post);
    }

    public Post save(Post post) {
        return repo.save(post);
    }

    public PostContent savePostContent(PostContent postContent) {
        return postMediaFileRepo.saveAndFlush(postContent);
    }

    public PostContent getPostMediaFile(Long mediaFileId, Post post) {
        return postMediaFileRepo.findByIdAndPost(mediaFileId, post).orElse(null);
    }

    public List<PostContent> getPostContents(Post post) {
        return postMediaFileRepo.findByPostAndActiveOrderByPos(post, 1);
    }

    public Post getById(Long postId) {
        return repo.findById(postId).orElse(null);
    }

    public Post getByIdAndStatus(Long id, int status){return repo.findByIdAndStatus(id,status).orElse(null);}

    // Get Stories or Posts
    public List<Post> getPosts(User user, Pageable pageable) {
        return repo.getPostsNative(user.getId(), pageable);
    }


    public List<Post> getUserStories(User user, Pageable pageable) {
        return repo.getUserStoriesNative(user.getId(), pageable);
    }

    public List<PostComment> getPostComments(Post post) {
        return postCommentRepo.findByPost(post);
    }

    public List<Post> getUserPosts(User user, Integer status,int type, Pageable pageable) {
        return repo.getUserPostsNative(user.getId(), status, type, pageable);
    }

    public List<Post> getUserPosts(Long userId, Integer type, Pageable pageable) {
        return repo.findByUserAndTypeAndStatus(userId, type, pageable);
    }

    public int countActivePost(User user) {
        return repo.countByUserAndStatusAndType(user, 1, EPostType.PERSONAL_POST);
    }



}
