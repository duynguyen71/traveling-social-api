package com.tc.notification.request;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionRequest {

    private List<String> tokens;

    private String topicName;

    private String title;

    private String target;

    private String body;

}
