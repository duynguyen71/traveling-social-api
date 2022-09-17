package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostEditDetailResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String contentJson;

    private double cost;

    private int numOfParticipant;

    private int totalDay;

    private int status;

    private FileUploadResponse coverPhoto;

    private List<ReviewPostEditAttachmentResponse> images = new LinkedList<>();

    private Set<TagResponse> tags = new LinkedHashSet<>();

    private LocationResponse location;

    private double myRating;


}
