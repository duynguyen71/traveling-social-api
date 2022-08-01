package com.tc.core.response;

import lombok.Data;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ReviewDetailResponse {

    private String title;

    private String detail;

    private Double cost;

    private Integer totalMember;

    private Integer totalDay;

    private Integer status;

    private FileUploadResponse coverPhoto;

    private Set<FileUploadResponse> photos = new LinkedHashSet<>();

    private UserInfoResponse user;

    private Date createDate;

}
