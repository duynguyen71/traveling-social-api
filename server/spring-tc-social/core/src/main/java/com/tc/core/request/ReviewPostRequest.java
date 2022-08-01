package com.tc.core.request;

import com.tc.core.model.FileUpload;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToOne;
import java.util.Date;
@Getter
@Setter
public class ReviewPostRequest {
    /* primitive */
    String title;

    String bio;

    Date departureDay;

    int totalDay;

    double cost;

    int participantsNumber;

}
