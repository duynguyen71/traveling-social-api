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
public class ReviewPostReport implements Serializable {

    private Long id;

    private String title;

    private FileUploadResponse coverPhoto;

    private int numOfVisitor;

    private int numOfComment;

    private int numOfLike;

    private Date createDate;

    private Date updateDate;

    private Double rating;
}
