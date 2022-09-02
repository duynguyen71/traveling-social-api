package com.tc.core.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewPostAttachmentRequest implements Serializable {

    private Long id;

    private int pos;

    private Long imageId;

    private String description;

    private int status = 1;

}
