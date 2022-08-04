package com.tc.tcapi.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUtil {

    public String createCommentNotificationMessage(String username,String comment){
        return username + " " + comment;
    }

}
