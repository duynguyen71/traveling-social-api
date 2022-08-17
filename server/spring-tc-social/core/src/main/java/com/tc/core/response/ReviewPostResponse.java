package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String contentJson;

    private Date departureDay;

    private int totalDay;

    private double cost;

    private int numOfParticipant;

    private int status;

    private int viewCount;

    private FileUploadResponse coverPhoto;

    private List<ReviewPostAttachmentResponse> images = new LinkedList();

    private List<TagResponse> tags = new LinkedList();

    private UserInfoResponse user;

    private Date createDate;

    private Date updateDate;


}
