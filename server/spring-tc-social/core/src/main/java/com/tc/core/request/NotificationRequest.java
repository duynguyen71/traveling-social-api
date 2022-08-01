package com.tc.core.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationRequest implements Serializable {

    @NotBlank
    private String target;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

}
