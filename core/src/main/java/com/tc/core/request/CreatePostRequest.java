package com.tc.core.request;

import com.tc.core.enumm.EPostType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePostRequest implements Serializable {

    private Long id;

    @NotNull
    private String caption;

    private Integer status = 1;

    @Enumerated(EnumType.ORDINAL)
    private EPostType type;

    private List<ContentUploadRequest> contents = new LinkedList<>();

    private Set<TagRequest> tags =new LinkedHashSet<>();

}
