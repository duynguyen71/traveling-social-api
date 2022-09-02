package com.tc.core.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse implements Serializable {

    private Long id;

    private String username;

    private String fullName;

    private String avt;

    private String email;

    private String bio;

    private String background;

    private Boolean isFollowing = false;
}
