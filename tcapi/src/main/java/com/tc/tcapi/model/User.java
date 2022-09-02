package com.tc.tcapi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String fullName;

    private String phone;

    private String email;

    private String password;

    private String verificationCode;

    private String avt;

    private int active;

    private int status;

    private String bio;

    private String website;

    private String background;

    private Integer isUsingApp;

    @Temporal(value = TemporalType.DATE)
    private Date birthdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    //
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadBy")
    private List<FileUpload> fileUploads = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower")
    private Set<Follow> following = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, targetEntity = Post.class, mappedBy = "user")
    private List<Post> posts = new ArrayList<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PostReaction> postReactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<UserReviewVisit> visitReviewPosts = new HashSet<>();

    @OneToMany(mappedBy = "notifier")
    private Collection<Notification> notifications = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewPost> reviewPosts = new ArrayList<>();

    @OneToMany(mappedBy = "actor")
    private Collection<NotificationActor> notificationActors;


    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    public User(Long id, String username, String fullName, String phone, String email, String password, String verificationCode, String avt, int active, int status, Date createDate, Date updateDate, Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.avt = avt;
        this.active = active;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.role = role;
    }

    @OneToMany(mappedBy = "user")
    private Collection<ReviewPostComment> reviewPostComments;

    @OneToMany(mappedBy = "user")
    private Collection<ReviewPostVisitor> reviewPostVisitors;

    @OneToMany(mappedBy = "user")
    private Collection<ReviewPostReaction> reviewPostReactions;

    @OneToMany(mappedBy = "user")
    private List<DeviceMetadata> deviceLists = new ArrayList<>();
}
