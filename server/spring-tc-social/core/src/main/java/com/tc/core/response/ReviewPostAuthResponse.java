package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostAuthResponse implements Serializable {

    private Long id;

    private String username;

    private String avt;

    private int numOfReviewPost;

    private int numOfPost;

    private int numOfFollower;

    private Boolean isFollowing;
}
