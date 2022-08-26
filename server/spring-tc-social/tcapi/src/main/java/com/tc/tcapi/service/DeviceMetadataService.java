package com.tc.tcapi.service;


import com.tc.tcapi.model.DeviceMetadata;
import com.tc.tcapi.repository.DeviceMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceMetadataService {

    private final DeviceMetadataRepository repo;

    public void save(DeviceMetadata deviceMetadata) {
        repo.save(deviceMetadata);
    }

    public List<DeviceMetadata> getDeviceList(Long userId) {
        return repo.findDeviceListNative(userId);
    }

    public DeviceMetadata findExistingDevice(Long userId, String deviceDetails, String location) {
        List<DeviceMetadata> deviceList = getDeviceList(userId);
        for (DeviceMetadata existingDevice :
                deviceList) {
            if (existingDevice.getDeviceDetail().equals(deviceDetails)) {
                return existingDevice;
            }
        }
        return null;
    }

    /**
     * browser (e.g. Chrome)
     * browserType (e.g. Browser or Application)
     * browserMajorVersion (e.g. 80 in case of Chrome)
     * deviceType (e.g. Mobile Phone, Desktop, Tablet, Console, TV Device)
     * platform (e.g. Android, iOS, Win7, Win8, Win10)
     * platformVersion (e.g. 4.2, 10 depending on what the platform is)
     */

}
