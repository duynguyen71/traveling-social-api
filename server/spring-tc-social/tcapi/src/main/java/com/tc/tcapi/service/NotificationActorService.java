package com.tc.tcapi.service;

import com.tc.tcapi.model.NotificationActor;
import com.tc.tcapi.model.NotificationObject;
import com.tc.tcapi.repository.NotificationActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationActorService {

    private final NotificationActorRepository repo;

    public NotificationActor saveFlush(NotificationActor actor) {
        return repo.saveAndFlush(actor);
    }
    public NotificationActor getByNotificationObject(NotificationObject notificationObject){
        return repo.findByNotificationObject(notificationObject).orElse(null);
    }
}
