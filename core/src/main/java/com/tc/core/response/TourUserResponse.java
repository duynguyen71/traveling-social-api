package com.tc.core.response;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TourUserResponse {

    private Long id;

    private BaseUserResponse user;

    private String createDate;

    private int status;

    private Long messageGroupId;

    private Date rejectedDate;

}
