package com.tc.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "device_metadata")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String deviceDetail;

    private String location;

    //    TODO : them token cho table
    // default firebase cloud message token
    private String token;

    private Date lastLoggedIn;


}
