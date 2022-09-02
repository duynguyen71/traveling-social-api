package com.tc.core.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContentUploadRequest implements Serializable {

    private Long id;

    private Integer pos;

    private String caption;

    private Long attachmentId;

    private Integer status = 1;

    private Integer active = 1;

    private AttachmentRequest attachment;
}
