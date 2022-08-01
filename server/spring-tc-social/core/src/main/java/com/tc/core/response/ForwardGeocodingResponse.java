package com.tc.core.response;

import com.tc.core.helpModel.ForwardGeocodingResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForwardGeocodingResponse {
    List<ForwardGeocodingResult> results;
}
