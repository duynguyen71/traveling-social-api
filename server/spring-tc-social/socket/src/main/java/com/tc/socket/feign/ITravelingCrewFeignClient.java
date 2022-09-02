package com.tc.socket.feign;

import com.tc.core.request.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "tcapi")
public interface ITravelingCrewFeignClient {

    @GetMapping("/api/v1/member/users/me")
    Object getCurrentUserLogged();

    @PostMapping("/api/v1/member/users/me/chat-groups/{groupId}/messages")
    Object saveMessage(@PathVariable("groupId") Long groupId, @RequestBody MessageRequest messageRequest);

}
