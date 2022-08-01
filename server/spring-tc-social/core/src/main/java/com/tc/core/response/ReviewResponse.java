package com.tc.core.response;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewResponse {

    private Long id;

    private String title;

    private FileUploadResponse coverPhoto;

    private UserInfoResponse user;

    private Date createDate;

}
