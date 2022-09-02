package com.tc.core.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest implements Serializable {

    private Long id;

    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;

    private String label;

    private String name;

    private String city;

    private String streetNumber;

    private String streetAddress;

    private String countryCode;

    private String countryName;

    private String region;

    private String postal;

}
