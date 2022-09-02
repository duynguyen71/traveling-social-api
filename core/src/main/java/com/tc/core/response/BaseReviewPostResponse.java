package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseReviewPostResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private FileUploadResponse coverPhoto;

    private UserInfoResponse user;

    private String location;

    private Date createDate;


}
