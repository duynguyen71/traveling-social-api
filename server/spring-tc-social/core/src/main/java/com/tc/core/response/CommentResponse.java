package com.tc.core.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {

    private Long id;

    private String content;

    private FileUploadResponse attachment;

    private UserInfoResponse user;

    private Date createDate;

    private Integer replyCount;


}
