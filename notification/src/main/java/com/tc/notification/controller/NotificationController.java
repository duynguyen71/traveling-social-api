package com.tc.notification.controller;

import com.tc.notification.helper.NotificationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tc.core.request.NotificationRequest;
@RestController
@RequestMapping("/api/v1/public")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private  final NotificationHelper helper;

    @PostMapping("/token")
    public ResponseEntity<?> sendPnsToDevice(@RequestBody NotificationRequest request) {
        log.info("Send notification to "+ request.getTarget());
        return helper.sendPnsToDevice(request);
    }
}
