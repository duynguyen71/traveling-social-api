package com.tc.tcapi.helper;

import com.tc.core.request.AddMemberRequest;
import com.tc.core.response.BaseResponse;
import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.ChatGroupUser;
import com.tc.tcapi.model.User;
import com.tc.tcapi.service.ChatGroupService;
import com.tc.tcapi.service.ChatGroupUserService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ChatGroupUserHelper")
@RequiredArgsConstructor
@Slf4j
public class ChatGroupUserHelper {

    private final ChatGroupUserService chatUsersService;
    private final UserService userService;
    private final ChatGroupService chatGroupService;

    public ResponseEntity<?> getChatGroupUserDetail(Long chatGroupUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        ChatGroupUser chatGroupUser = chatUsersService.getChatGroupUser(chatGroupUserId, currentUserId);
        return null;
    }

    public ResponseEntity<?> addMember(Long groupId, AddMemberRequest request) {
        ChatGroup currentUserChatGroup = chatGroupService.getGroup(groupId, userService.getCurrentUser(), 1);
        if (currentUserChatGroup == null) {
            return BaseResponse.badRequest("Can not find group with id: " + groupId);
        }
        for (Long id :
                request.getIds()) {
            ChatGroupUser chatGroupUser = new ChatGroupUser();
            User user = userService.getById(id);
            chatGroupUser.setUser(user);
            chatGroupUser.setStatus(1);
            chatGroupUser.setChatGroup(currentUserChatGroup);
            chatUsersService.save(chatGroupUser);
        }

        return BaseResponse.success("Add user: " + request.toString() + " to group success!");
    }

    public ResponseEntity<?> leaveGroup(Long groupId) {
        ChatGroupUser chatGroupUser = chatUsersService.findByUserAndChatGroupId(userService.getCurrentUser(), groupId);
        if (chatGroupUser == null) {
            return BaseResponse.badRequest("Can not find group with id: " + groupId);
        }
        chatGroupUser.setStatus(0);
        chatUsersService.save(chatGroupUser);
        return BaseResponse.success("Leave chat group: " + groupId + " success!");
    }
}
