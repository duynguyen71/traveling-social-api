package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse implements Serializable {

    private Long id;

    private String username;

    private String fullName;

    private String avt;

    private String email;

    private int followerCounts;

    private int followingCounts;

    private String bio;

    private LocationResponse location;

    private String background;

    private String website;

    private Date birthdate;

    private Date createDate;


}
