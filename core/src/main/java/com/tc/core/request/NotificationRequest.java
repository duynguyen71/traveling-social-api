package com.tc.core.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationRequest implements Serializable {

    private String target;

    private String title;

    private String body;

}
