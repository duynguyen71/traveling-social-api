package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String contentJson;

    private double cost;

    private int numOfParticipant;

    private int totalDay;

    private int status;

    private FileUploadResponse coverImage;

    private Set<ReviewPostAttachmentResponse> images = new LinkedHashSet<>();

    private BaseUserResponse user;

    private Date createDate;

}
