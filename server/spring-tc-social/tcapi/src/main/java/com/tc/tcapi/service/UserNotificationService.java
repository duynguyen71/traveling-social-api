package com.tc.tcapi.service;

import com.tc.tcapi.model.Notification;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final NotificationRepository repo;

    public List<Notification> getUserNotifications(User user){
        return repo.findAllByNotifierOrderByCreateDateDesc(user);
    }

    public void save(Notification notification){
        repo.save(notification);
    }

}
