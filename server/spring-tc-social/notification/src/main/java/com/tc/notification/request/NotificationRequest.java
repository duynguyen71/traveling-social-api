package com.tc.notification.request;

import lombok.Data;

@Data
public class NotificationRequest {


    private String target;

    private String title;

    private String body;

}
