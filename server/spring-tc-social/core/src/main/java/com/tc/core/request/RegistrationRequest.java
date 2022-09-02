package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @NotBlank
    @Size(min = 6, max = 25)
    private String username;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;

    @NotBlank
    @Email
    private String email;
}
