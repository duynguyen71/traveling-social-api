package com.tc.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
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

    private String title;

    private String bio;

    private Date departureDay;

    private int totalDay;

    private double cost = 0;

    private int numberOfParticipants = 0;
    
    private int status;
    
    @OneToOne
    @JoinColumn(name = "cover_image_id")
    private FileUpload coverImage;

    @OneToMany(mappedBy = "reviewPost")
    private List<ReviewPostImage> images = new LinkedList<>();

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "reviewPosts")
    private List<Tag> reviewPostTags = new ArrayList<Tag>();

    @Column
    @CreatedBy
    private String createdBy;

    @Column
    @CreatedDate
    private Date createdDate;

    @Column
    @LastModifiedDate
    private Date modifiedDate;

    @Column
    @LastModifiedBy
    private String modifiedBy;


}
