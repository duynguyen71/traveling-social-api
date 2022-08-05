package com.tc.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "review_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @ManyToOne
    @JoinColumn(name = "cover_image_id")
    private FileUpload coverImage;

    @NotNull
    private String content;

    @NotNull
    private String contentJson;

    private Date departureDay;

    private int totalDay;

    private double cost = 0;

    private int numOfParticipant = 0;

    private int status = 1;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "reviewPost")
    private List<ReviewPostImage> images = new LinkedList<>();

    @ManyToMany(mappedBy = "reviewPosts")
    private List<Tag> reviewPostTags = new LinkedList<>();


}
