package com.tc.tcapi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "notification")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notifier_id")
    private User notifier;

    @ManyToOne
    @JoinColumn(name = "notification_object_id")
    private NotificationObject notificationObject;

    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;


}
