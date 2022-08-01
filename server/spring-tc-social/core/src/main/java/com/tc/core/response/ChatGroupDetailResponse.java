package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatGroupDetailResponse implements Serializable {

    private Long id;

    private String name;

    private Set<UserProfileResponse> users = new LinkedHashSet<>();

    private MessageResponse lastMessage;

    private Date createDate;

    private Date updateDate;

}
