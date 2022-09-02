package com.tc.tcapi.utilities;

import com.tc.tcapi.model.DeviceMetadata;
import com.tc.tcapi.service.DeviceMetadataService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceMetadataUtil {

    private final DeviceMetadataService service;
    private final UserService userService;

    public void extractAndSaveDeviceMetadata(HttpServletRequest request) {

        Parser uaParser = new Parser();
        Client client = uaParser.parse(request.getHeader(HttpHeaders.USER_AGENT));
        DeviceMetadata deviceMetadata = new DeviceMetadata();
        // Device name
        String deviceName = client.device.family;
        deviceMetadata.setDeviceName(deviceName);
        // OS
        final String operatingSystem = client.os.family;
        deviceMetadata.setOperatingSystem(operatingSystem);
        final String osVersion = client.os.major;
        deviceMetadata.setOSMajor(osVersion);
        final String oSMinor = client.os.minor;
        deviceMetadata.setOSMinor(oSMinor);
        // Browser
        final String browser = client.userAgent.family;
        deviceMetadata.setBrowser(browser);
        final String browserMajor = client.userAgent.major;
        deviceMetadata.setBrowserMajor(browserMajor);
        final String browserMinor = client.userAgent.minor;
        deviceMetadata.setBrowserMinor(browserMinor);
        //
        String deviceDetail = deviceName + " - " + browser
                + " " + browserMajor + "."
                + browserMinor + " - "
                + operatingSystem + " " + osVersion
                + "." + oSMinor;
        deviceMetadata.setDeviceDetail(deviceDetail);
        // get push notification token
        String pushNotificationToken = request.getHeader("pnt");
        deviceMetadata.setToken(pushNotificationToken);
        //
        deviceMetadata.setUser(userService.getCurrentUser());
        DeviceMetadata existingDevice = service.findExistingDevice(userService.getCurrentUser().getId(), deviceDetail, null);
        if (existingDevice != null) {
            existingDevice.setToken(pushNotificationToken);
            service.save(existingDevice);
        } else {
            service.save(deviceMetadata);
        }
    }


}
