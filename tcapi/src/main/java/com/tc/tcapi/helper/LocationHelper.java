package com.tc.tcapi.helper;

import com.tc.core.request.LocationRequest;
import com.tc.core.response.BaseResponse;
import com.tc.tcapi.model.ELocationType;
import com.tc.tcapi.model.Location;
import com.tc.tcapi.model.User;
import com.tc.tcapi.service.LocationService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationHelper {

    private final LocationService service;
    private final UserService userService;

    public ResponseEntity<?> updateCurrentUserLocation(LocationRequest request) {
        User currentUser = userService.getCurrentUser();
        Location location = service.getByUser(currentUser.getId());
        if (location == null) {
            location = new Location();
        }
        location.setLongitude(request.getLongitude());
        location.setLatitude(request.getLatitude());
        location.setCity(request.getCity());
        location.setCountryCode(request.getCountryCode());
        location.setCountryName(request.getCountryName());
        location.setRegion(request.getRegion());
        location.setStreetAddress(request.getStreetAddress());
        location.setStreetNumber(request.getStreetNumber());
        location.setType(1);
        location.setStatus(1);
        location = service.saveFlush(location);
        currentUser.setLocation(location);
        userService.save(currentUser);
        return BaseResponse.success("Update location success");
    }
}
