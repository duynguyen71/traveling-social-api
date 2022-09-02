package com.tc.tcapi.repository;

import com.tc.tcapi.model.NotificationActor;
import com.tc.tcapi.model.NotificationObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationActorRepository extends JpaRepository<NotificationActor, Long> {

    Optional<NotificationActor> findByNotificationObject(NotificationObject notificationObject);
}
