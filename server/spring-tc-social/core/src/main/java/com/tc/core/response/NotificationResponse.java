package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationResponse implements Serializable {

    private Long id;

    private String title;

    private String body;

    private String createDate;

    private Integer status;
}
