package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuestionPostResponse implements Serializable {

    private Long id;

    private String caption;

    private Integer status;

    private Integer active;

    private UserInfoResponse user;

    private Long reactionCount;

    private Date createDate;

    private Integer likeCount;

    private Integer commentCount;

    private boolean isClose;

    private List<TagResponse> tags = new ArrayList<>();

}
