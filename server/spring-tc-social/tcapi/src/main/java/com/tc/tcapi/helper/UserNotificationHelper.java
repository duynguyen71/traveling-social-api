package com.tc.tcapi.helper;

import com.tc.core.model.Notification;
import com.tc.core.model.NotificationObject;
import com.tc.core.model.User;
import com.tc.core.request.NotificationRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.NotificationResp;
import com.tc.core.response.NotificationResponse;
import com.tc.tcapi.service.NotificationObjectService;
import com.tc.tcapi.service.PostService;
import com.tc.tcapi.service.UserNotificationService;
import com.tc.tcapi.service.UserService;
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
    private final PostService postService;

    /**
     * Get current user notifications
     */
    public ResponseEntity<?> getCurrentUserNotifications() {
        User user = userService.getCurrentUser();
        List<NotificationObject> notificationObjects = notificationObjectService.getNotificationObjects(user);
        List<NotificationResp> data = notificationObjects.stream()
                .map(n -> modelMapper.map(n, NotificationResp.class))
                .collect(Collectors.toList());


        //group by notification_entity type
        Map<Long, List<NotificationObject>> groupByEntityType =
                notificationObjects.stream()
                        .collect(Collectors.groupingBy((notificationObject) -> notificationObject.getEntityTypeId()));
//        groupByEntityType.get()
        for (Long key :
                groupByEntityType.keySet()) {
            //fetch post
//            if (key >= 0 || key <= 3) {
//                postService.getById()
//            }
            List<NotificationObject> objects = groupByEntityType.get(key);
        }
        log.info("group by entity type - {}", groupByEntityType);
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
