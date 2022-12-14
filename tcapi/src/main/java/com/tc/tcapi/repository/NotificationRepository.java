package com.tc.tcapi.repository;

import com.tc.tcapi.model.Notification;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByNotifierOrderByCreateDateDesc(User user);
}
