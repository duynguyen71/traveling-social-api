package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse implements Serializable {

    private Long id;

    private String content;

    private FileUploadResponse attachment;

    private UserInfoResponse user;

    private UserInfoResponse replyUser;

    private Date createDate;

    private Integer replyCount;

}
