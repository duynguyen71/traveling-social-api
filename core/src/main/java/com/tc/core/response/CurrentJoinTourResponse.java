package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentJoinTourResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private int numOfMember;

    private double cost;

    private int totalDay;

    private int joinedMember;

    private int numOfRequest;

    private LocationResponse location;

    private List<TourUserResponse> users;

    private Date departureDate;

    private BaseUserResponse host;


}
