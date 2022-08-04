package com.tc.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Types of notifications
 */
@Table(name = "notification_object")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "entity_id")
    private Long entityId;

    /**
     * identify the type of notification
     */
    @JoinColumn(name = "entity_type_id")
    private Long entityTypeId;

    @Column(name = "message")
    private String message;

    //
    @OneToMany(mappedBy = "notificationObject")
    private Collection<NotificationActor> notificationActors = new ArrayList();

    @OneToMany(mappedBy = "notificationObject")
    private Collection<Notification> notifications = new ArrayList();


}
