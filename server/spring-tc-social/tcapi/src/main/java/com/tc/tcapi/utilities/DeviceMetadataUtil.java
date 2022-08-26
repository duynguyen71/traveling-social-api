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

//            UserAgent agent = UserAgent.parseUserAgentString("CFNetwork/897.15 Darwin/17.5.0 (iPhone/6s iOS/11.3)");
//            UserAgent agent = UserAgent.parseUserAgentString("Dalvik/2.1.0 (Linux; U; Android 5.1.1; Android SDK built for x86 Build/LMY48X)");
//            UserAgent agent = UserAgent.parseUserAgentString(agentHeader);
        Parser uaParser = new Parser();
        Client client = uaParser.parse(request.getHeader(HttpHeaders.USER_AGENT));
//            Client client = uaParser.parse("CFNetwork/897.15 Darwin/17.5.0 (iPhone/6s iOS/11.3)");
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
