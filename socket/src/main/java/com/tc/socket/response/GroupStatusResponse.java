package com.tc.socket.response;

import com.tc.core.response.BaseUserResponse;
import com.tc.socket.enumm.GroupStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupStatusResponse implements Serializable {

    @Enumerated(EnumType.STRING)
    private GroupStatus status;

    private BaseUserResponse user;

    private Date createDate;

}
