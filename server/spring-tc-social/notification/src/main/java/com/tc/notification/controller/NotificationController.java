package com.tc.notification.controller;

import com.tc.notification.helper.NotificationHelper;
import com.tc.notification.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private  final NotificationHelper helper;

    @PostMapping("/token")
    public ResponseEntity<?> sendPnsToDevice(@RequestBody NotificationRequest request) {
        return helper.sendPnsToDevice(request);
    }
}
