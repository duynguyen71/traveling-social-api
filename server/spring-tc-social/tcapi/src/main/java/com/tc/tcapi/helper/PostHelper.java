package com.tc.tcapi.helper;

import com.tc.core.model.*;
import com.tc.core.request.*;
import com.tc.core.response.*;
import com.tc.tcapi.utilities.ObjectMapperUtils;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("PostHelper")
@RequiredArgsConstructor
@Slf4j
public class PostHelper {

    private final UserService userService;
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    private final PostReactionService postReactionService;
    private final PostCommentService postCommentService;
    private final ObjectMapperUtils om;
    private final ReviewPostService reviewPostService;
    private final FileService fileService;

    //    /* Create review post*/
//    @Transactional
//    public synchronized  ResponseEntity<?> createReviewPost(ReviewPostRequest reviewPostRequest, Long coverImageId,Long[] imageIds){
    public ResponseEntity<?> createReviewPost(ReviewPostRequest reviewPostRequest, Long coverImageId, Long[] imageIds) {
//        ReviewPost reviewPost = this.om.map(reviewPostRequest, ReviewPost.class);
//        ReviewPost savedReviewPost = reviewPostService.save(reviewPost);
//        FileUpload coverImage = fileStorageService.getById(coverImageId);
//        List<FileUpload> reviewPostImage = new ArrayList<>();
//        for (Long imageId : imageIds) {
//            FileUpload foundFile = fileService.findOne(imageId);
//            if (foundFile != null){
//                reviewPostImage.add(foundFile);
////                foundFile.setReviewPost(savedReviewPost);
//            }
//        }
////        savedReviewPost.setReviewPostImages(reviewPostImage);
//        savedReviewPost.setCoverImage(coverImage);
//        return BaseResponse.success(savedReviewPost.getId(), "CREATED");
        return null;
    }

    /**
     * Lay ra cac bai post cua user hien tai
     *
     * @param params
     * @return
     */
    public ResponseEntity<?> getCurrentUserPosts(Map<String, String> params) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(params);

        Pageable pageable = baseParamRequest.toPageRequest();

        List<Post> posts = postService.getUserPosts(currentUser, baseParamRequest.getStatus(), pageable);
        List<PostResponse> rs = posts.stream()
                .map(post -> {
                    PostResponse postResponse = modelMapper.map(post, PostResponse.class);
                    //lay ra so luong reactions
                    Long reactionCount = postReactionService.countReactions(post);
                    postResponse.setReactionCount(reactionCount);
                    //get current user reaction on post
                    PostReaction postReaction = postReactionService.getByUser(currentUser, post, 1);
                    if (postReaction != null)
                        postResponse.setMyReaction(modelMapper.map(postReaction.getReaction(), ReactionResponse.class));
                    return postResponse;
                }).collect(Collectors.toList());

        return BaseResponse.success(rs, "get user id: " + currentUser.getId() + "\'s posts success!");
    }

    public ResponseEntity<?> createPost(CreatePostRequest createPostRequest) {
        log.info("create post...");
        User currentUser = userService.getCurrentUser();
        String postCaption = createPostRequest.getCaption();
        Long postRequestId = createPostRequest.getId();
        List<ContentUploadRequest> contents =
                createPostRequest.getContents();
        String messageResponse = "create post success";
        Post post;
        if (postRequestId != null && (post = postService.getById(postRequestId)) != null) {
            messageResponse = "update post success";
        } else {
            post = new Post();
            post.setUser(currentUser);
        }
        post.setCaption(postCaption);
        post.setStatus(1);
        post.setType(createPostRequest.getType());

        post = postService.savePostAndFlush(post);

        for (int i = 0; i < contents.size(); i++) {
            ContentUploadRequest contentRequest = contents.get(i);
            Long postContentId = contentRequest.getId();
            AttachmentRequest attachmentRequest = contentRequest.getAttachment();
            if (attachmentRequest != null) {
                FileUpload fileUpload;
                if (attachmentRequest.getId() != null) {
                    fileUpload = fileStorageService.getById(attachmentRequest.getId());
                } else if (attachmentRequest.getName() != null) {
                    fileUpload = fileStorageService.getByName(attachmentRequest.getName());
                } else {
                    continue;
                }
                if (fileUpload == null) {
                    continue;
                }
                PostContent postContent;
                Integer pos = contentRequest.getPos();
                if (postContentId != null &&
                        (postContent = postService.getPostMediaFile(postContentId, post)) != null) {
                } else {
                    postContent = new PostContent();
                    postContent.setPost(post);
                    postContent.setAttachment(fileUpload);
                }
                postContent.setActive(contentRequest.getActive());
                postContent.setCaption(contentRequest.getCaption());
                postContent.setStatus(1);
                postContent.setPos(pos != null ? pos : i);
                postService.savePostContent(postContent);
            }
        }
        log.info("create post success");
        //map to post response
        PostResponse postResponse = modelMapper.map(post, PostResponse.class);
        postResponse.setUser(modelMapper.map(currentUser, UserInfoResponse.class));
        //get contents
        List<PostContent> postContents = postService.getPostContents(post);
        postResponse.setContents(
                postContents.stream()
                        .map(postContent -> {
                            PostContentResponse postContentResponse = modelMapper.map(postContent, PostContentResponse.class);
                            FileUploadResponse map = modelMapper.map(postContent.getAttachment(), FileUploadResponse.class);
                            postContentResponse.setAttachment(map);
                            return postContentResponse;
                        })
                        .collect(Collectors.toList())
        );


        return BaseResponse.success(postResponse, messageResponse);
    }

    public ResponseEntity<?> getPostComments(Long postId) {

        List<PostComment> postComments = postService.getPostComments(postService.getById(postId));

        List<CommentResponse> data = postComments.stream()
                .map(comment -> {
                    CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
                    return commentResponse;
                })
                .collect(Collectors.toList());
        return BaseResponse.success(data, "Get post comments with post id: " + postId + " success!");
    }

    public ResponseEntity<?> getStories(Map<String, String> params) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(params);

        Pageable pageable = baseParamRequest.toPageRequest();
        List<Post> posts = postService.getUserStories(currentUser, pageable);
        List<PostResponse> data = posts.stream()
                .map(post -> {
                    PostResponse postResponse = modelMapper.map(post, PostResponse.class);
                    List<PostContent> postContents = postService.getPostContents(post);
                    postResponse.setContents(
                            postContents.stream()
                                    .map(content -> modelMapper.map(content, PostContentResponse.class))
                                    .collect(Collectors.toList())
                    );
                    postResponse.setLikeCount(1);
                    return postResponse;
                })
                .collect(Collectors.toList());

        return BaseResponse.success(data, "get current user's stories success");
    }


    public ResponseEntity<?> getPosts(Map<String, String> params) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(params);
        Pageable pageable = baseParamRequest.toPageRequest();
        List<Post> posts = postService.getPosts(currentUser, pageable);
        List<PostResponse> data = posts.stream()
                .map(post -> {
                    PostResponse postResponse = modelMapper.map(post, PostResponse.class);
                    postResponse.setReactionCount(postReactionService.countReactions(post));
                    postResponse.setCommentCount(postCommentService.countAllComments(post));
                    //get current user reaction on post
                    PostReaction postReaction = postReactionService.getByUser(currentUser, post, 1);
                    if (postReaction != null)
                        postResponse.setMyReaction(modelMapper.map(postReaction.getReaction(), ReactionResponse.class));
                    //get current users comments on post
                    List<PostComment> myComments = postCommentService.getCurrentUserPostComments(post, currentUser, 1);
                    postResponse.setMyComments(
                            myComments.stream()
                                    .map(c -> {
                                        PropertyMap<PostComment, CommentResponse> clientPropertyMap = new PropertyMap<>() {
                                            @Override
                                            protected void configure() {
                                                skip(destination.getUser());
                                            }
                                        };
                                        if (modelMapper.getTypeMap(PostComment.class, CommentResponse.class) == null)
                                            modelMapper.addMappings(clientPropertyMap);
                                        CommentResponse commentResponse = modelMapper.map(c, CommentResponse.class);
                                        commentResponse.setReplyCount(postCommentService.countReply(c));
                                        return commentResponse;
                                    })
                                    .collect(Collectors.toList())
                    );
                    //lay ra noi dung bai post
                    List<PostContent> postContents = postService.getPostContents(post);
                    postResponse.setContents(
                            postContents.stream()
                                    .map(content -> {
                                        PostContentResponse postContentResponse = modelMapper.map(content, PostContentResponse.class);
                                        FileUploadResponse map = modelMapper.map(content.getAttachment(), FileUploadResponse.class);
                                        postContentResponse.setAttachment(map);
                                        return postContentResponse;
                                    })
                                    .collect(Collectors.toList())
                    );
                    return postResponse;
                })
                .collect(Collectors.toList());

        return BaseResponse.success(data, "get current user's post success");
    }


    public ResponseEntity<?> updateStatus(Long postId, Integer status) {
        Post post = postService.getCurrentUserPost(postId, userService.getCurrentUser());
        if (post != null) {
            post.setStatus(status);
            postService.save(post);
            return BaseResponse.success("update post " + post + " status success!");
        }
        return BaseResponse.badRequest("could not find post with id: " + postId);
    }

    public ResponseEntity<?> getUserPosts(Long userId, Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageRequest = baseParamRequest.toPageRequest();
        List<Post> posts = postService.getUserPosts(userId, 1, pageRequest);
        List<PostResponse> data = posts.stream()
                .map(post -> {
                    PostResponse postResponse = modelMapper.map(post, PostResponse.class);
                    postResponse.setReactionCount(postReactionService.countReactions(post));
                    postResponse.setCommentCount(postCommentService.countAllComments(post));
                    //get  currentUser reaction on post
                    PostReaction postReaction = postReactionService.getByUser(currentUser, post, 1);
                    if (postReaction != null)
                        postResponse.setMyReaction(modelMapper.map(postReaction.getReaction(), ReactionResponse.class));
                    //get current users comments on post
                    List<PostComment> myComments = postCommentService.getCurrentUserPostComments(post, currentUser, 1);
                    postResponse.setMyComments(
                            myComments.stream()
                                    .map(c -> {
                                        PropertyMap<PostComment, CommentResponse> clientPropertyMap = new PropertyMap<>() {
                                            @Override
                                            protected void configure() {
                                                skip(destination.getUser());
                                            }
                                        };
                                        if (modelMapper.getTypeMap(PostComment.class, CommentResponse.class) == null)
                                            modelMapper.addMappings(clientPropertyMap);
                                        CommentResponse commentResponse = modelMapper.map(c, CommentResponse.class);
                                        commentResponse.setReplyCount(postCommentService.countReply(c));
                                        return commentResponse;
                                    })
                                    .collect(Collectors.toList())
                    );
                    //lay ra noi dung bai post
                    List<PostContent> postContents = postService.getPostContents(post);
                    postResponse.setContents(
                            postContents.stream()
                                    .map(content -> {
                                        PostContentResponse postContentResponse = modelMapper.map(content, PostContentResponse.class);
                                        FileUploadResponse map = modelMapper.map(content.getAttachment(), FileUploadResponse.class);
                                        postContentResponse.setAttachment(map);
                                        return postContentResponse;
                                    })
                                    .collect(Collectors.toList())
                    );
                    return postResponse;
                })
                .collect(Collectors.toList());

        return BaseResponse.success(data, "get user id:" + userId + " posts success");
    }


}
