package com.tc.core.response;

import lombok.Data;

import java.util.Date;

@Data
public class PostContentResponse {

    private Long id;

    private String caption;

    private FileUploadResponse attachment;

    private Date createDate;

    private Date updateDate;
}
