package com.tc.tcapi.repository;

import com.tc.tcapi.model.NotificationActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationActorRepository extends JpaRepository<NotificationActor, Long> {
}
