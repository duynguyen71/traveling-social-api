package com.tc.core.response;

import com.tc.core.model.FileUpload;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReviewPostResponse {
    String title;

    String bio;

    Date departureDay;

    int totalDay;

    double cost;

    int participantsNumber;

    FileUploadResponse coverImage;

    List<FileUploadResponse> reviewPostImages;
}
