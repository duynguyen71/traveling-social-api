package com.tc.tcapi.model;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Table(name = "device_metadata")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeviceMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoggedIn;

    private String deviceName;

    private String operatingSystem;

    @Column(name = "os_major")
    private String oSMajor;

    @Column(name = "os_minor")
    private String oSMinor;

    private String browser;

    private String browserMinor;

    private String browserMajor;

    private String deviceDetail;

    private String location;

    // Notification for firebase messaging
    private String token;
}
