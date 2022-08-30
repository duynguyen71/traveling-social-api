package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationResp implements Serializable {

    //notification object id
    private Long id;

    private Long entityTypeId;

    private String entityTypeName;

    private Long entityId;

    private String message;

    private Date createDate;

    private BaseUserResponse user;

}
