package com.tc.tcapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "follow")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Follow  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @UpdateTimestamp
    @Column(name = "unfollow_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unFollowDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date followDate;

    private Integer status = 1;

    private Integer active =1;


}
