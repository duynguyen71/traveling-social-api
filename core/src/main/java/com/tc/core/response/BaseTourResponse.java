package com.tc.core.response;

import com.tc.core.request.TagRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseTourResponse {

    private Long id;

    private String title;

    private String content;

    private int numOfMember;

    private int joinedMember;

    private Date createDate;

    private Set<TagResponse> tags = new HashSet<>();


}
