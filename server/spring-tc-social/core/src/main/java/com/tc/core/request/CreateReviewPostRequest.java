package com.tc.core.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateReviewPostRequest implements Serializable {

    private Long id;

    @NotBlank
    private String title;

    private double cost = 0.0;

    private int numOfParticipant = 1;

    private int totalDay = 1;

    private Set<TagRequest> tags = new HashSet<>();

    @NotBlank
    private String contentJson;

    @NotBlank
    private String content;

    @NotNull
    private Long coverImageId;

    private List<ReviewPostAttachmentRequest> images = new LinkedList<>();


}
