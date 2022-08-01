package com.tc.socket.config;

import com.tc.socket.controller.AuthToken;
import com.tc.socket.feign.ITravelingCrewFeignClient;
import com.tc.socket.model.MyUserPrinciple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

    private final AuthToken authToken;
    private final ITravelingCrewFeignClient feignClient;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Instantiate an object for retrieving the STOMP headers
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // Check that the object is not null
        assert accessor != null;
        StompCommand command = accessor.getCommand();
        if (command == StompCommand.CONNECT) {
            //get bearer token
            final String bearerToken = accessor.getFirstNativeHeader("Authorization");
            authToken.setBearerToken(bearerToken);
            try {
                Map<String, Object> baseResponse = (Map<String, Object>) feignClient.getCurrentUserLogged();
                Map<String, Object> userMap = (Map<String, Object>) baseResponse.get("data");
                MyUserPrinciple myUserPrinciple = new MyUserPrinciple((String) userMap.get("username"));
                myUserPrinciple.setAvt((String) userMap.get("avt"));
                myUserPrinciple.setBearerToken(bearerToken);
                accessor.setUser(myUserPrinciple);
                log.info("Current user logged in - {}", myUserPrinciple.getName());
            } catch (Exception e) {
                log.info("Failed to get user info - {}", e);
            }
        } else if (command == StompCommand.SUBSCRIBE) {
            authToken.setBearerToken(((MyUserPrinciple) accessor.getUser()).getBearerToken());
            String destination = accessor.getDestination();
            log.info("User {} subscribe to destination : {}", accessor.getUser().getName(), destination);
        } else if (command == StompCommand.SEND) {
            authToken.setBearerToken(((MyUserPrinciple) accessor.getUser()).getBearerToken());
            String destination = accessor.getDestination();
            log.info("User {} send to destination : {}", accessor.getUser().getName(), destination);
        }
        return message;
    }
}
