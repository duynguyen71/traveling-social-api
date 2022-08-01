package com.tc.core.response;

import com.tc.core.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceMetadataResponse implements Serializable {

    private Long id;

    private String deviceDetail;

    private String location;

    private String token;

    private Date lastLoggedIn;
}
