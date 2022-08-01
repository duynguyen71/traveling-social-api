package com.tc.tcapi.feign;

//connect to notification service to notify states changed
public interface NotificationFeign {

    /**
     * send notification when user has new message
     */
    void sendNewMessageNotification();


}
