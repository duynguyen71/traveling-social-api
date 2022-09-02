package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostEditAttachmentResponse implements Serializable {

    private Long id;

    private Long imageId;

    private Integer status;

    private String name;

    private Integer pos;
}
