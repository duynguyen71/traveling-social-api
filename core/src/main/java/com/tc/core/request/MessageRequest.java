package com.tc.core.request;

import com.tc.core.enumm.EMessageType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest implements Serializable {

    private String message;

    private Long replyMessageId;

    private String sendFromUser;

    private List<Long> attachments = new LinkedList<>();

    private Long sendToUserId;

    private String sendToUser;

    @Enumerated(EnumType.ORDINAL)
    private EMessageType eMessageType;


}
