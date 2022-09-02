package com.tc.tcapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review_post_reaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_post_id", nullable = false)
    private ReviewPost reviewPost;

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false)
    private Reaction reaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;
}
