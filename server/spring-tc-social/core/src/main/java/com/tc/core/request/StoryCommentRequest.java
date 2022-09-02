package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StoryCommentRequest {

    private Long id;

    @NotBlank
    private String content;

    //1 public comment, 2 private comment
    @NotNull
    private Integer type = 1;


}
