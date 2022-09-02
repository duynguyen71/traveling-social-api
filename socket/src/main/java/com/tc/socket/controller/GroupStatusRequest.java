package com.tc.socket.controller;

import com.tc.socket.enumm.GroupStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupStatusRequest implements Serializable {

    private Long groupId;

    @Enumerated(EnumType.STRING)
    private GroupStatus status;


}
