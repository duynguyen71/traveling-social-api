package com.tc.tcapi.service;

import com.tc.tcapi.model.Notification;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> notificationList(User user
    ) {
        return notificationRepository.findAllByNotifierOrderByCreateDateDesc(user);
    }
}
