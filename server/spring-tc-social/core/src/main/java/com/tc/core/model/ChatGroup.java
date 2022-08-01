package com.tc.core.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Table(name = "chat_group")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroup  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @OneToMany(mappedBy = "chatGroup")
    private Set<ChatGroupUser> users = new LinkedHashSet<>();

    @OneToMany(mappedBy = "chatGroup")
    private Set<Message> messages = new LinkedHashSet<>();

    public void addChatGroupUser(ChatGroupUser user) {
        this.users.add(user);
        user.setChatGroup(this);
    }

}
