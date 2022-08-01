package com.tc.core.response;

import com.tc.core.helpModel.Prediction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceAutocompleteResponse {
    List<Prediction> predictions;
}
