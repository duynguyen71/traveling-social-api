package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatGroupDetailResponse implements Serializable {

    private Long id;

    private String name;

    private List<BaseUserResponse> users = new LinkedList<>();

    private MessageResponse lastMessage;

    private Date createDate;

    private Date updateDate;

}
