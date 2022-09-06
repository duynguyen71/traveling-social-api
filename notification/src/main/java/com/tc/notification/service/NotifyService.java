package com.tc.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.tc.core.request.NotificationRequest;
import com.tc.notification.request.SubscriptionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotifyService {

    private final FirebaseApp firebaseApp;

    public String sendPnsToDevice(NotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.getTarget())
                .putData("target", request.getTarget())
                .putData("title", request.getTitle())
                .putData("body", request.getBody())
                .build();
        String response;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e.getMessage());
            return null;
        }
    }

    public void subscribeToTopic(SubscriptionRequest subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail", e);
        }
    }

    public void unsubscribeFromTopic(SubscriptionRequest subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail", e);
        }
    }

    public String sendPnsToTopic(SubscriptionRequest request) {
        Message message = Message.builder()
                .setTopic(request.getTarget())
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody()).build())
                .putData("content", request.getTitle())
                .putData("body", request.getBody())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }
        return response;
    }
}
