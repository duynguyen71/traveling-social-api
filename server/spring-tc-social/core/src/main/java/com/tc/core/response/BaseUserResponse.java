package com.tc.core.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseUserResponse implements Serializable {

    private Long id;

    private String username;

    private String avt;



}
