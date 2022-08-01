package com.tc.core.request;

import lombok.Data;

@Data
public class PostCommentRequest {

    private Long id;

    private Integer status = 1;

    private String content;

    private Long attachmentId;

    private Long parentCommentId;

}
