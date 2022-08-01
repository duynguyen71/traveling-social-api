package com.tc.core.response;

import lombok.Data;

import java.util.Date;

@Data
public class PostReactionResponse {

    private Long id;

    private  ReactionResponse reaction;

    private Date createDate;

    private UserInfoResponse user;
}
