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
@Table(name = "review_post_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private FileUpload image;

    //đùng để sắp xếp vị trí của ảnh
    private Integer pos;

    private Integer status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "review_post_id")
    private ReviewPost reviewPost;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;


}
