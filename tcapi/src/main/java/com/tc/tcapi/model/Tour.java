package com.tc.tcapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

@Table(name = "tour")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int numOfMember;

    private Date departureDate;

    private double cost;

    private boolean isClose;

    private int status;

    private int totalDay;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany
    @JoinTable(name = "tour_tag", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "tour")
    private Collection<TourReaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "tour")
    private Collection<TourComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "tour")
    private Collection<TourUser> tourUsers = new ArrayList<>();


}
