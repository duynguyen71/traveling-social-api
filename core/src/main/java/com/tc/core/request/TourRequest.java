package com.tc.core.request;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TourRequest {

    private Long id;

    private String title;

    private String content;

    private int numOfMember;

    private Date departureDate;

    private LocationRequest location;

    private double cost;

    private boolean isClose = false;

    private int status;

    private Set<TagRequest> tags = new HashSet<>();

    private int totalDay = 1;

}
