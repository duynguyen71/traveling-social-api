package com.tc.core.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse implements Serializable {


    private Long id;

    private String username;

    private String fullName;

    private String avt;

    private int followerCounts;

    private int followingCounts;

    private String bio;

    private String website;

    private Date birthdate;

    private String background;

    private Date createDate;

    private Boolean isFollowing = false;

    private LocationResponse location;
}
