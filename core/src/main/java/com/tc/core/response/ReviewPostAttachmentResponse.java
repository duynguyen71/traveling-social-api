package com.tc.core.response;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostAttachmentResponse implements Serializable {

    private Long id;

    private FileUploadResponse image;

    private Integer status;

    private Integer pos;
}
