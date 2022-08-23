package com.tc.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateBaseInfoRequest {

    private String fullName;

    private String username;

    private String bio;

    private Long avtId;

    private Long backgroundId;

    private String website;

    private String birthdate;

}
