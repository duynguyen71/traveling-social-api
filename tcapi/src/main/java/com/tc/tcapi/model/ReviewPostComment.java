package com.tc.tcapi.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "review_post_comment")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private FileUpload attachment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private ReviewPost post;

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
    private ReviewPostComment parent;

    @OneToMany(mappedBy = "parent")
    private List<ReviewPostComment> answers = new ArrayList<>();

}
