package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {


    @NotBlank(message = "username may not be blank")
    @Size(min = 4, max = 15)
    private String username;

    @NotBlank(message = "full name may not be blank")
    @Size(min = 10, max = 30)
    private String fullName;

    private String phone;

}
