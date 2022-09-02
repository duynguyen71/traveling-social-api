package com.tc.core.response;

import com.tc.core.enumm.EMessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class MessageResponse implements Serializable {

    private Long id;

    private String message;

    @Enumerated(EnumType.ORDINAL)
    private EMessageType type;

    private Integer status;

    private BaseUserResponse user;

    private Date createDate;

    private MessageResponse replyMessage;
}
