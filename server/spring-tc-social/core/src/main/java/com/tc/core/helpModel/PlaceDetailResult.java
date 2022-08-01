package com.tc.core.helpModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDetailResult {
    String formatted_address;
    Geometry geometry;
    String place_id;
}
