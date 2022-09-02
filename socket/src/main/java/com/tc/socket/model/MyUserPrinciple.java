package com.tc.socket.model;

import lombok.Setter;

import java.io.Serializable;
import java.security.Principal;
@Setter
public class MyUserPrinciple implements Principal, Serializable {

    private Long id;
    private String username;
    private String avt;
    private String bearerToken;

    public MyUserPrinciple(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }


    public Long getId() {
        return id;
    }

    public String getAvt() {
        return avt;
    }

    public String getBearerToken(){
        return this.bearerToken;
    }
}
