package com.tc.tcapi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "tour_comment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TourComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private TourComment parent;

    @OneToMany(mappedBy = "parent")
    private List<TourComment> answers = new ArrayList<>();

}
