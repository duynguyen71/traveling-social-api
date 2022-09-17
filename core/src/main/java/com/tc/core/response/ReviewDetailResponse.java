package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String contentJson;

    private double cost;

    private int numOfParticipant;

    private int totalDay;

    private int numOfVisitor;

    private int numOfReaction;

    private int numOfComment;

    private boolean hasBookmark;

    private int status;

    private double rating;

    private double myRating;

    private ReactionResponse reaction;

    private FileUploadResponse coverPhoto;

    private Set<ReviewPostAttachmentResponse> images = new LinkedHashSet<>();

    private Set<TagResponse> tags = new LinkedHashSet<>();

    private BaseUserResponse user;

    private Date createDate;

    private LocationResponse location;

}
