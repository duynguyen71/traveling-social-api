package com.tc.tcapi.utilities;

import com.tc.core.request.NotificationRequest;
import com.tc.tcapi.feign.NotificationFeign;
import com.tc.tcapi.model.*;
import com.tc.tcapi.service.NotificationActorService;
import com.tc.tcapi.service.NotificationObjectService;
import com.tc.tcapi.service.UserNotificationService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationUtil {

    private final UserNotificationService notificationService;
    private final NotificationFeign notificationFeign;
    private final UserService userService;
    private final NotificationObjectService notificationObjectService;
    private final NotificationActorService notificationActorService;


    public void sendReactionNotification(String postTitle, User... users) {
        User currentUser = userService.getCurrentUser();
        String messageBody = currentUser.getUsername() + " " +
                "vừa yêu thích bài viết của bạn!\n" + postTitle;

        NotificationObject notificationObject = new NotificationObject();
        notificationObject.setMessage(messageBody);
        notificationObject = notificationObjectService.saveFlush(notificationObject);

        NotificationActor notificationActor = new NotificationActor();
        notificationActor.setActor(currentUser);
        notificationActor.setNotificationObject(notificationObject);
        notificationActor = notificationActorService.saveFlush(notificationActor);

        for (User user :
                users) {
            if (user.getDeviceLists() != null) {
                log.info("Send notification to user " + user.getUsername());
                for (DeviceMetadata deviceMetadata :
                        user.getDeviceLists()) {
                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setTarget(deviceMetadata.getToken());
                    notificationRequest.setTitle("Yêu thích mới");
                    notificationRequest.setBody(messageBody);
                    //

                    //
                    if (user.getId() != currentUser.getId()) {
                        try {
                            notificationFeign.sendNewMessageNotification(notificationRequest);

                        } catch (Exception e) {
                            log.info("Failed to send message notification {}", e.getMessage());

                        }
                    }
                }
            }

            if (user.getId() != currentUser.getId()) {
                Notification notification = new Notification();
                notification.setNotifier(user);
                notification.setStatus(1);
                notification.setNotificationObject(notificationObject);
                notificationService.save(notification);
            }

        }
    }

    public void sendCommentNotification(String cmt, User... users) {
        User currentUser = userService.getCurrentUser();
        //object
        String messageBody = currentUser.getUsername() + " " +
                "vừa bình luận bài viết của bạn: " + cmt;
        NotificationObject notificationObject = new NotificationObject();
        notificationObject.setMessage(messageBody);
        notificationObject = notificationObjectService.saveFlush(notificationObject);

        NotificationActor notificationActor = new NotificationActor();
        notificationActor.setActor(currentUser);
        notificationActor.setNotificationObject(notificationObject);
        notificationActor = notificationActorService.saveFlush(notificationActor);
        for (User user :
                users) {
            if (user.getDeviceLists() != null) {
                log.info("Send comment notification to user " + user.getUsername());
                for (DeviceMetadata deviceMetadata :
                        user.getDeviceLists()) {
                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setTarget(deviceMetadata.getToken());
                    notificationRequest.setTitle("Bình luận mới!");
                    notificationRequest.setBody(messageBody);

                    //
                    if (user.getId() != currentUser.getId()) {
                        try {
                            notificationFeign.sendNewMessageNotification(notificationRequest);
                        } catch (Exception e) {
                            log.info("Failed to send message notification {}", e.getMessage());
                        }
                    }

                }
            }
            //
            if (user.getId() != currentUser.getId()) {
                Notification notification = new Notification();
                notification.setNotifier(user);
                notification.setStatus(1);
                notification.setNotificationObject(notificationObject);
                notificationService.save(notification);
            }

        }


        //actor

    }
}

