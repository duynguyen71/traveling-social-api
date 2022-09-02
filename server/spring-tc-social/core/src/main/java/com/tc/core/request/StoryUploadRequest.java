package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StoryUploadRequest {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Long cover;


}
