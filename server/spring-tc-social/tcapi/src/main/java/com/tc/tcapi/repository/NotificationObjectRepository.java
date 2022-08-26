package com.tc.tcapi.repository;

import com.tc.tcapi.model.NotificationObject;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationObjectRepository extends JpaRepository<NotificationObject, Long> {

    List<NotificationObject> findAllByNotifications_NotifierOrderByNotifications_CreateDateDesc(User notifier);


}
