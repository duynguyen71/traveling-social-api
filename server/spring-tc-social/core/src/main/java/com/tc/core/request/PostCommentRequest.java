package com.tc.core.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCommentRequest implements Serializable {

    private Long id;

    private Integer status = 1;

    private String content;

    private Long attachmentId;

    private Long parentCommentId;

}
