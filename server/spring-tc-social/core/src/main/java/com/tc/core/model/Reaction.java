package com.tc.core.model;

import com.tc.core.enumm.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "reaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reaction  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private EReactionType type;

    @OneToMany(mappedBy = "reaction",fetch = FetchType.LAZY)
    private List<PostReaction> posts = new ArrayList<>();


    @OneToMany(mappedBy = "reaction",fetch = FetchType.LAZY)
    private Collection<ReviewPostReaction> reviewPostReactions;

}
