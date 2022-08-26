package com.tc.tcapi.feign;

import com.tc.core.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//connect to notification service to notify states changed
@FeignClient(name = "notification")
public interface NotificationFeign {

    /**
     * send notification when user has new message
     */
    @PostMapping("/api/v1/public/token")
    void sendNewMessageNotification(@RequestBody NotificationRequest notificationRequest);


}
