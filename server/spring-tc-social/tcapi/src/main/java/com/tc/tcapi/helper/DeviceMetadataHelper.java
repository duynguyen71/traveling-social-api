package com.tc.tcapi.helper;

import com.tc.tcapi.model.DeviceMetadata;
import com.tc.tcapi.model.User;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.UserDeviceMetadataResponse;
import com.tc.tcapi.service.DeviceMetadataService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceMetadataHelper {

    private final DeviceMetadataService service;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> getDeviceList() {
        User user = userService.getCurrentUser();
        List<DeviceMetadata> deviceList = service.getDeviceList(user.getId());
        List<UserDeviceMetadataResponse> data = deviceList.stream()
                .map(d -> modelMapper.map(d, UserDeviceMetadataResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(data, "get user devices success");
    }

}
