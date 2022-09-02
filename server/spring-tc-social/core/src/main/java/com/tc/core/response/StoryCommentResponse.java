package com.tc.core.response;

import com.tc.core.enumm.EStoryCommentType;
import lombok.Data;

import java.util.Date;

@Data
public class StoryCommentResponse {

    private Long id;

    private String content;

    private EStoryCommentType type;

    private Date createDate;
}
