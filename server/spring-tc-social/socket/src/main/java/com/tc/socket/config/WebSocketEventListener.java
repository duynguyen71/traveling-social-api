package com.tc.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectedEvent) {
        String username = sessionConnectedEvent.getUser().getName();
        log.info("Received new socket connection from user : {}", username);
    }

    @EventListener
    public void handleWebSocketDisconnectionEventListener(SessionDisconnectEvent sessionDisconnectEvent) {
        String username = sessionDisconnectEvent.getUser().getName();
        log.info("user {} disconnected!", username);
    }

}
