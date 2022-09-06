package com.tc.core.response;

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
public class TourDetailResponse {

    private Long id;

    private String title;

    private String content;

    private int numOfMember;

    private double cost;

    private int totalDay;

    private int joinedMember;

    private Set<TagResponse> tags = new HashSet<>();

    private LocationResponse location;

    private Date departureDate;


}
