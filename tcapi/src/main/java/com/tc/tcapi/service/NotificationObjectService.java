package com.tc.tcapi.service;

import com.tc.tcapi.model.NotificationObject;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.NotificationObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationObjectService {

    private final NotificationObjectRepository repo;

    public List<NotificationObject> getNotificationObjects(User notifier){
        return repo.findAllByNotifications_NotifierOrderByNotifications_CreateDateDesc(notifier);
    }

    public NotificationObject saveFlush(NotificationObject notificationObject){
        return repo.saveAndFlush(notificationObject);
    }

}
