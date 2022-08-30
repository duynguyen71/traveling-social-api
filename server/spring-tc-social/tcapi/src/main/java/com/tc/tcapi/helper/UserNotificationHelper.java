package com.tc.tcapi.helper;

import com.tc.core.response.BaseUserResponse;
import com.tc.tcapi.model.Notification;
import com.tc.tcapi.model.NotificationActor;
import com.tc.tcapi.model.NotificationObject;
import com.tc.tcapi.model.User;
import com.tc.core.request.NotificationRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.NotificationResp;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("UserNotificationHelper")
@RequiredArgsConstructor
@Slf4j
public class UserNotificationHelper {

    private final UserNotificationService service;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final NotificationObjectService notificationObjectService;
    private final NotificationActorService notificationActorService;

    /**
     * Get current user notifications
     */
    public ResponseEntity<?> getCurrentUserNotifications() {
        User user = userService.getCurrentUser();
        List<Notification> notifications = service.getUserNotifications(user);
        List<NotificationResp> data = notifications.stream()
                .map(notification -> {
                    NotificationResp map = modelMapper.map(notification, NotificationResp.class);
                    NotificationObject notificationObject = notification.getNotificationObject();
                    String message = notificationObject.getMessage();
                    map.setMessage(message);
                    NotificationActor notificationActor = notificationActorService.getByNotificationObject(notificationObject);
                    if (notificationActor != null) {
                        map.setUser(modelMapper.map(notificationActor.getActor(), BaseUserResponse.class));
                    }
                    log.info("{}", map);
                    return map;
                }).collect(Collectors.toList());
        return BaseResponse.success(data, "Get notifications success!");
    }

    /**
     * Save notification
     */
    public ResponseEntity<?> saveNotification(NotificationRequest request) {
        Notification notification = new Notification();
//        notification.setUser(userService.getCurrentUser());
//        notification.setStatus(1);
//        notification.setTitle(request.getTitle());
//        notification.setBody(request.getBody());
        service.save(notification);
        return BaseResponse.success("Save notification success!");
    }


}
