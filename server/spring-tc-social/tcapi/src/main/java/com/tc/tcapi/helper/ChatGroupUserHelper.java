package com.tc.tcapi.helper;

import com.tc.core.model.ChatGroupUser;
import com.tc.tcapi.service.ChatGroupUserService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("ChatGroupUserHelper")
@RequiredArgsConstructor
@Slf4j
public class ChatGroupUserHelper {

    private final ChatGroupUserService chatUsersService;
    private final UserService userService;


    public ResponseEntity<?> getChatGroupUserDetail(Long chatGroupUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        ChatGroupUser chatGroupUser = chatUsersService.getChatGroupUser(chatGroupUserId, currentUserId);
        return null;
    }
}
