package com.tc.notification.helper;

import com.tc.notification.request.NotificationRequest;
import com.tc.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHelper {

    private final NotifyService service;

    public ResponseEntity<?> sendPnsToDevice(NotificationRequest request) {
        String str = service
                .sendPnsToDevice(request);
        return ResponseEntity.ok(str);
    }


}
