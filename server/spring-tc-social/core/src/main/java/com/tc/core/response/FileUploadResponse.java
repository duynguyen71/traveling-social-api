package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileUploadResponse implements Serializable {

    private Long id;

    private String name;

    private String contentType;

    private Date createDate;

}
