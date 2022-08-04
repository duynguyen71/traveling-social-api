package com.tc.core.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Table(name = "notification_actor")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationActor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_object_id")
    private NotificationObject notificationObject;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private User actor;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
