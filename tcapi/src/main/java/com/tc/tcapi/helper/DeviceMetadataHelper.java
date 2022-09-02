package com.tc.tcapi.helper;

import com.tc.core.request.UpdateDevice;
import com.tc.tcapi.model.DeviceMetadata;
import com.tc.tcapi.model.User;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.UserDeviceMetadataResponse;
import com.tc.tcapi.service.DeviceMetadataService;
import com.tc.tcapi.service.UserService;
import com.tc.tcapi.utilities.DeviceMetadataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceMetadataHelper {

    private final DeviceMetadataService service;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DeviceMetadataUtil deviceMetadataUtil;

    public ResponseEntity<?> getDeviceList() {
        User user = userService.getCurrentUser();
        List<DeviceMetadata> deviceList = service.getDeviceList(user.getId());
        List<UserDeviceMetadataResponse> data = deviceList.stream()
                .map(d -> modelMapper.map(d, UserDeviceMetadataResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(data, "get user devices success");
    }

    public ResponseEntity<?> updateDeviceInfo(HttpServletRequest request) {
        log.info("Update notification token user",
                userService.getCurrentUser().getUsername());
        deviceMetadataUtil.extractAndSaveDeviceMetadata(request);
        return ResponseEntity.ok().build();
    }
}
