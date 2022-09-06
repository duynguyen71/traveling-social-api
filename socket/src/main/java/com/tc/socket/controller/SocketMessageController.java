package com.tc.socket.controller;

import com.tc.core.request.MessageRequest;
import com.tc.core.response.BaseUserResponse;
import com.tc.core.response.MessageResponse;
import com.tc.socket.feign.ITravelingCrewFeignClient;
import com.tc.socket.model.MyUserPrinciple;
import com.tc.socket.response.GroupStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;


@Controller
@Slf4j
@RequiredArgsConstructor
public class SocketMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ITravelingCrewFeignClient feignClient;

    @MessageMapping("/news")
    @SendTo("/topic/news")
    public MessageResponse broadcastMessage(SimpMessageHeaderAccessor accessor, @Payload MessageRequest req) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(req.getMessage());
        //
        MyUserPrinciple myUserPrinciple = (MyUserPrinciple) accessor.getUser();
        //
        BaseUserResponse baseUserResponse = new BaseUserResponse();
        baseUserResponse.setUsername(myUserPrinciple.getName());
        baseUserResponse.setAvt(myUserPrinciple.getAvt());
        messageResponse.setUser(baseUserResponse);
        return messageResponse;
    }

    @MessageMapping("/groups/{groupId}")
    public MessageResponse messageResponse(SimpMessageHeaderAccessor accessor,
                                           @DestinationVariable("groupId") Long groupId,
                                           @Payload MessageRequest req) {
        log.info("\nSend message...");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(req.getMessage());
        MyUserPrinciple myUserPrinciple = (MyUserPrinciple) accessor.getUser();
        BaseUserResponse baseUserResponse = new BaseUserResponse();
        baseUserResponse.setUsername(myUserPrinciple.getName());
        baseUserResponse.setAvt(myUserPrinciple.getAvt());
        messageResponse.setUser(baseUserResponse);
        messageResponse.setCreateDate(new Date());
        simpMessagingTemplate.convertAndSend("/queue/groups/" + groupId, messageResponse);
        saveMessage(groupId, req);
        return messageResponse;
    }

    @Async
    void saveMessage(Long groupId, MessageRequest req) {
        log.info("Save message...\n");
        try {
            feignClient.saveMessage(groupId, req);
        } catch (Exception e) {
            log.info("Failed to save message {}", e);
        }
    }


    @MessageMapping("/groups/{groupId}/status")
    public GroupStatusResponse sendGroupStatus(SimpMessageHeaderAccessor accessor,
                                               @DestinationVariable("groupId") Long groupId,
                                               @Payload GroupStatusRequest req) {
        GroupStatusResponse resp = new GroupStatusResponse();
        //
        MyUserPrinciple myUserPrinciple = (MyUserPrinciple) accessor.getUser();
        //
        BaseUserResponse baseUserResp = new BaseUserResponse();
        baseUserResp.setUsername(myUserPrinciple.getName());
        baseUserResp.setAvt(myUserPrinciple.getAvt());
        resp.setUser(baseUserResp);
        resp.setStatus(req.getStatus());
        simpMessagingTemplate.convertAndSend("/queue/groups/" + groupId + "/status", resp);
        return resp;
    }

    @MessageMapping("/sendTo")
    public void send(SimpMessageHeaderAccessor sha, @Payload MessageRequest messageRequest) {
        String sendFrom = sha.getUser().getName();
        log.info("\nSending message from {}", sendFrom);
        log.info("Message : {}\n", messageRequest);
        simpMessagingTemplate.convertAndSendToUser(messageRequest.getSendToUser(), "/queue/messages", messageRequest);
    }
}
