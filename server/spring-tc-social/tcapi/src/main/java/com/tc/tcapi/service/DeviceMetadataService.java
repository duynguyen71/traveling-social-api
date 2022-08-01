package com.tc.tcapi.service;

import com.tc.core.model.DeviceMetadata;
import com.tc.core.model.User;
import com.tc.tcapi.repository.DeviceMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceMetadataService {

    private final DeviceMetadataRepository repo;

    public void save(DeviceMetadata deviceMetadata) {
        repo.save(deviceMetadata);
    }

    public List<DeviceMetadata> getDeviceList(Long userId) {
        return repo.findAllByUser_Id(userId);
    }

    public DeviceMetadata findExistingDevice(Long userId, String deviceDetails, String location) {
        List<DeviceMetadata> deviceList = getDeviceList(userId);
        for (DeviceMetadata existingDevice :
                deviceList) {
            if (existingDevice.getDeviceDetail().equals(deviceDetails)
                    && existingDevice.getLocation().equals(location)) {
                return existingDevice;
            }
        }
        return null;
    }
}
