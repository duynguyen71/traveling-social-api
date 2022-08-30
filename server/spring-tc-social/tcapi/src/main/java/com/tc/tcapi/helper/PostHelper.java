package com.tc.tcapi.helper;

import com.tc.core.enumm.EPostType;
import com.tc.core.utilities.ValidationUtil;
import com.tc.tcapi.model.*;
import com.tc.core.request.*;
import com.tc.core.response.*;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final ReviewPostService reviewPostService;
    private final FileService fileService;
    private final TagService tagService;

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
        List<Post> posts = postService.getUserPosts(currentUser, baseParamRequest.getStatus(), 1, pageable);
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
                    // count comment
                    int commentCount = postCommentService.countAllComments(post);
                    postResponse.setCommentCount(commentCount);
                    return postResponse;
                }).collect(Collectors.toList());

        return BaseResponse.success(rs, "get user id: " + currentUser.getId() + "\'s posts success!");
    }

    public ResponseEntity<?> getCurrentUserQuestions(Map<String, String> params) {
        User currentUser = userService.getCurrentUser();
        BaseParamRequest baseParamRequest = new BaseParamRequest(params);
        Pageable pageable = baseParamRequest.toPageRequest();
        List<Post> posts = postService.getUserPosts(currentUser, 1, 2, pageable);
        List<QuestionPostResponse> questionPostResponses = convertToQuestionPostsResponse(posts);
        return BaseResponse.success(questionPostResponses, "Get current user question posts success!");
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
        // save tags
        Set<TagRequest> tags = createPostRequest.getTags();
        log.info("tag request {}", tags);
        for (TagRequest tagRequest :
                tags) {
            Tag tag;
            String tagReqName = tagRequest.getName();
            tag = tagService.getByName(tagReqName);
            if (tag == null) {
                tag = tagService.saveFlush(Tag.builder().name(tagReqName).build());
            }
            post.getTags().add(tag);
            postService.save(post);
        }
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
        if (posts.isEmpty() && baseParamRequest.getPage() == 0) {
            posts = postService.findPosts(1, EPostType.STORY);
        }
        List<Post> copy = new ArrayList<>(posts);
        List<Post> randomPosts = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < Math.min(baseParamRequest.getPageSize(), posts.size()); i++) {
            randomPosts.add(copy.remove(secureRandom.nextInt(copy.size())));
        }
        List<PostResponse> data = randomPosts.stream()
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
        if (posts.isEmpty() && baseParamRequest.getPage() == 0) {
            posts = postService.findPosts(1, EPostType.PERSONAL_POST);
        }
        List<Post> copy = new ArrayList<>(posts);
        List<Post> randomPosts = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < Math.min(baseParamRequest.getPageSize(), posts.size()); i++) {
            randomPosts.add(copy.remove(secureRandom.nextInt(copy.size())));
        }
        List<PostResponse> data = randomPosts.stream()
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


    //search posts
    public ResponseEntity<?> searchPosts(Map<String, String> params) {
        BaseParamRequest paramRequest = new BaseParamRequest(params);
        String keyWord = null;
        if (!ValidationUtil.isNullOrBlank(params.get("keyWord"))) {
            keyWord = params.get("keyWord");
        }
        List<Post> posts = postService.searchPosts("%" + keyWord + "%", "%" + keyWord + "%", 2, paramRequest.toPageRequest());
        List<QuestionPostResponse> data = convertToQuestionPostsResponse(posts);
        return BaseResponse.success(data, "Search posts with keyword: " + keyWord + " success!");
    }

    private List<QuestionPostResponse> convertToQuestionPostsResponse(List<Post> posts) {
        List<QuestionPostResponse> data = posts.stream().map(
                post -> {
                    QuestionPostResponse response = mapToQuestionPostResponse(post);
                    int i = postCommentService.countAllComments(post);
                    response.setCommentCount(i);
                    return response;
                }
        ).collect(Collectors.toList());
        return data;
    }

    @NotNull
    private QuestionPostResponse mapToQuestionPostResponse(Post post) {
        QuestionPostResponse response = modelMapper.map(post, QuestionPostResponse.class);
        response.setReactionCount(postReactionService.countReactions(post));
        response.setCommentCount(postCommentService.countAllComments(post));
        log.info("Post close {}", post.isClose());
        response.setClose(post.isClose());
        return response;
    }

    public ResponseEntity<?> getQuestionPostDetail(Long postId) {
        // get active post
        Post post = postService.getByIdAndStatus(postId, 1);
        if (post == null) return BaseResponse.badRequest("Can not find post with id: " + postId);
        QuestionPostResponse postResponse = mapToQuestionPostResponse(post);
        return BaseResponse.success(postResponse, "Get question detail id:" + postId + " success");
    }

    public ResponseEntity<?> closeQuestionPost(Long questionPostId, int status) {
        Post post = postService.getCurrentUserPost(questionPostId, userService.getCurrentUser());
        if (post == null) return BaseResponse.badRequest("Can not find question post with id: " + questionPostId);
        post.setClose(status == 1);
        postService.save(post);
        return BaseResponse.success("Close comment on question post id: " + questionPostId + " success");
    }

    public ResponseEntity<?> getQuestions(Map<String, String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageable = baseParamRequest.toNativePageRequest("createDate");
        List<Post> posts = postService.findAllPosts(EPostType.QUESTION, 1, pageable);
        List<QuestionPostResponse> data = posts.stream()
                .map(post -> mapToQuestionPostResponse(post)).collect(Collectors.toList());
        return BaseResponse.success(data,null);
    }
}
