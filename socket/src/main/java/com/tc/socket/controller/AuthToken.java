package com.tc.socket.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class AuthToken {

    private String bearerToken;

}
