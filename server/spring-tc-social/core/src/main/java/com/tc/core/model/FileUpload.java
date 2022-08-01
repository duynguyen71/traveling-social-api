package com.tc.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "file_upload")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileUpload{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

    private String ext;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    private Integer active;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "upload_by")
    private User uploadBy;

    @OneToMany(mappedBy = "attachment", fetch = FetchType.LAZY)
    private List<PostContent> postContents = new ArrayList<>();

    @OneToMany(mappedBy = "attachment", fetch = FetchType.LAZY)
    private List<PostComment> postComments = new ArrayList<>();

    @ManyToMany(mappedBy = "photos")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "coverPhoto")
    private List<Review> reviewCoverPhotos = new ArrayList<>();

//    @OneToOne(mappedBy = "coverImage")
//    private ReviewPost coverReviewPost;
//
//    @ManyToOne
//    @JoinColumn(name = "review_post_id")
//    private ReviewPost reviewPost;

}
