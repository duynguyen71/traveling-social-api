package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse implements Serializable {

    private Long id;

    private double latitude;

    private double longitude;

    private String label;

    private String city;

}
