package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewRequest {

    private Long id;

    @NotNull
    @Size(min = 20, max = 250)
    private String title;

    @NotNull
    @Size(
            min = 200
    )
    private String detail;

    @NotNull(message = "numericField: positive number value is required")
    @Min(value = 0, message = "numericField: positive number, min 0 is required")
    private Long coverPhoto;

    private Integer totalMember;

    private Double cost;

    private Integer totalDay;

    private List<Long> photos = new ArrayList<>();

}
