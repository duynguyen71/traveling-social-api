package com.tc.core.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttachmentRequest implements Serializable {

    private Long id;

    private String name;

}
