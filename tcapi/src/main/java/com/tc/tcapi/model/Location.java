package com.tc.tcapi.model;

import com.tc.core.enumm.EPostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "location")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;

    private Double latitude;

    private String name;

    private String label;

    private String city;

    private String streetNumber;

    private String streetAddress;

    private String countryCode;

    private String countryName;

    private String region;

    private String postal;

    private int status;

    private int type;

    @OneToOne(mappedBy = "location")
    private User user;

    @UpdateTimestamp
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "create_date")
    private Date createDate;

    @OneToMany(mappedBy = "location")
    private List<ReviewPost> reviewPosts = new ArrayList<>();


}
