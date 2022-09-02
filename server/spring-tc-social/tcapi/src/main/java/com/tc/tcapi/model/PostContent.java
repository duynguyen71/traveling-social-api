package com.tc.tcapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "post_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostContent  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "caption", nullable = false)
    private String caption;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachment_id")
    @Where(clause = "active = 1")
    private FileUpload attachment;

    private Integer pos;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Integer active;

    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
