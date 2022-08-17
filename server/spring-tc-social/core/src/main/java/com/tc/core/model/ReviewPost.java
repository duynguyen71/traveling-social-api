package com.tc.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

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
    @JoinColumn(name = "cover_photo_id")
    private FileUpload coverPhoto;

    @NotNull
    private String content;

    @NotNull
    private String contentJson;

    private Date departureDay;

    private int totalDay;

    private double cost = 0;

    private int numOfParticipant = 0;

    private int status = 1;

    @ManyToMany
    @JoinTable(name = "review_post_tag",
            joinColumns = {@JoinColumn(name = "review_post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;

    @OneToMany(mappedBy = "reviewPost", fetch = FetchType.LAZY)
    private List<ReviewPostImage> images = new LinkedList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Collection<ReviewPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "reviewPost", fetch = FetchType.LAZY)
    private Collection<ReviewPostVisitor> visitors;

    @OneToMany(mappedBy = "reviewPost")
    private Collection<ReviewPostReaction> reactions = new ArrayList<>();


}
