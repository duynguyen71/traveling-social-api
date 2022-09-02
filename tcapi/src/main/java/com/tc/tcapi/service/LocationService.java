package com.tc.tcapi.service;

import com.tc.tcapi.model.ELocationType;
import com.tc.tcapi.model.Location;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepo;

    public void save(Location location) {
        locationRepo.save(location);
    }

    public Location saveFlush(Location location) {
        return locationRepo.saveAndFlush(location);
    }

    public Location getByUser(Long userId) {
        return locationRepo.findByUser_IdAndType(userId, 1)
                .orElse(null);
    }

    public List<Location> getPopularLocations() {
        return locationRepo.findAllByType(2);
    }


}
