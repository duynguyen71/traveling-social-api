package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationResp implements Serializable {

    //notification object id
    private Long id;

    private Long entityTypeId;

    private String entityTypeName;

    private Long entityId;

    private String message;

    private Date createDate;

}
