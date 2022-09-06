package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TourUserBadRequestResponse {

    private String message;

    public static TourUserBadRequestResponse userNotAvailable() {
        return new TourUserBadRequestResponse("USER_NOT_AVAILABLE");
    }
}


