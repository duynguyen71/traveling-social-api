package com.tc.core.response;

import lombok.Data;

import java.util.Date;

@Data
public class StoryInfoResponse {

    private Long id;

    private String description;

    private FileUploadResponse cover;

    private Date createDate;

    private Boolean isViewed;

}
