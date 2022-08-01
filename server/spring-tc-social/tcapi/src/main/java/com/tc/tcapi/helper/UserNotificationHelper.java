package com.tc.tcapi.helper;

import com.tc.core.model.Notification;
import com.tc.core.model.User;
import com.tc.core.request.NotificationRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.NotificationResponse;
import com.tc.tcapi.service.UserNotificationService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("UserNotificationHelper")
@RequiredArgsConstructor
public class UserNotificationHelper {

    private final UserNotificationService service;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /**
     * Get current user notifications
     */
    public ResponseEntity<?> getCurrentUserNotifications() {
        User user = userService.getCurrentUser();
        List<Notification> notifications = service.getUserNotifications(user);
        List<NotificationResponse> data = notifications.stream()
                .map(n -> modelMapper.map(n, NotificationResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(data, "Get notifications success!");
    }

    /**
     * Save notification
     */
    public ResponseEntity<?> saveNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUser(userService.getCurrentUser());
        notification.setStatus(1);
        notification.setTitle(request.getTitle());
        notification.setBody(request.getBody());
        service.save(notification);
        return BaseResponse.success("Save notification success!");
    }


}
