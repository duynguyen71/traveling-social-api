package com.tc.tcapi.service;

import com.tc.core.model.NotificationObject;
import com.tc.core.model.User;
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

}
