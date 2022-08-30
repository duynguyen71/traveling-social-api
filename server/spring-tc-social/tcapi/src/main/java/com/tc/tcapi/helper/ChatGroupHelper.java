package com.tc.tcapi.helper;

import com.tc.core.request.UpdateGroupInfoRequest;
import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.ChatGroupUser;
import com.tc.tcapi.model.Message;
import com.tc.tcapi.model.User;
import com.tc.core.request.ChatGroupCreateRequest;
import com.tc.core.response.*;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.ChatGroupService;
import com.tc.tcapi.service.ChatGroupUserService;
import com.tc.tcapi.service.MessageService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component("ChatGroupHelper")
public class ChatGroupHelper {

    private final ChatGroupService service;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final ChatGroupUserService chatUserService;
    private final ChatGroupUserService chatGroupUserService;


    //get current user chat groups
    public ResponseEntity<?> getChatGroups(Map<String, String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageRequest = baseParamRequest.toNativePageRequest("createDate");
        List<ChatGroup> groups = service.getGroups(userService.getCurrentUser(), 1, pageRequest);
        List<ChatGroupResponse> chatGroupResponses = groups.stream()
                .map(g -> {
                    ChatGroupResponse chatGroupResponse = modelMapper.map(g, ChatGroupResponse.class);
                    //get last message
                    Message lastMessage = messageService.getLastMessage(g, 1);
                    if (lastMessage != null) {
                        chatGroupResponse.setLastMessage(modelMapper.map(lastMessage, MessageResponse.class));
                    }
                    //get users
                    List<ChatGroupUser> chatGroupUsers = chatUserService.getChatUsers(g, 1);
                    Set<BaseUserResponse> users = chatGroupUsers.stream()
                            .map(c -> modelMapper.map(c.getUser(), BaseUserResponse.class))
                            .collect(Collectors.toSet());
                    chatGroupResponse.setUsers(users);
                    //
                    return chatGroupResponse;
                })
                .collect(Collectors.toList());
        String message = "get chat groups success!" + "length: " + groups.size();
        log.info("{}", message);
        return BaseResponse.success(chatGroupResponses, message);
    }

    public ResponseEntity<?> createChatGroup(ChatGroupCreateRequest request) {
        List<Long> memberIds = request.getUserIds();
        if (memberIds == null || memberIds.isEmpty()) {
            return BaseResponse.badRequest("create group failed: member users is empty");
        }
//        TODO: loi tu tao chat grop voi chinh  minh
        User currentUser = userService.getCurrentUser();
        ChatGroup chatGroup = null;
        //check exist group between two users
        if (memberIds.size() == 1) {
            List<Long> list = memberIds.stream().toList();
            chatGroup = service.getGroupByTwoUsers(list.get(0), currentUser.getId());
            //return bad request if group between two users exists
            if (chatGroup != null) {
                return BaseResponse.badRequest("Group between two user exist");
            }
        }
        //create new chat group not exist
        chatGroup = new ChatGroup();
        chatGroup.setStatus(1);
        chatGroup.setName(request.getName());
        chatGroup = service.saveFlush(chatGroup);
        ChatGroupUser chatGroupUser = new ChatGroupUser();
        chatGroupUser.setUser(currentUser);
        chatGroupUser.setChatGroup(chatGroup);
        chatGroupUser.setStatus(1);
        chatGroupUser = chatGroupUserService.saveFlush(chatGroupUser);
        chatGroup.addChatGroupUser(chatGroupUser);
        for (Long id :
                memberIds) {
            User user = userService.getById(id, 1);
            if (user != null) {
                ChatGroupUser groupUser = new ChatGroupUser();
                groupUser.setUser(user);
                groupUser.setStatus(1);
                chatGroup.getUsers().add(groupUser);
                groupUser.setChatGroup(chatGroup);
                chatGroupUser.setChatGroup(chatGroup);
                groupUser = chatGroupUserService.saveFlush(groupUser);
                chatGroup.addChatGroupUser(groupUser);
            }
        }
        service.save(chatGroup);
        return BaseResponse.success("create chat group success");
    }

    public ResponseEntity<?> createChatGroupWithNames(ChatGroupCreateRequest request) {
        List<String> memberNames = request.getNames();
        if (memberNames == null || memberNames.isEmpty()) {
            return BaseResponse.badRequest("create group failed: member users is empty");
        }
        User currentUser = userService.getCurrentUser();
        ChatGroup chatGroup = null;
        //check exist group between two users
        if (memberNames.size() == 1) {
            String name = memberNames.get(0);
            User byUsername = userService.getByUsername(name.trim());
            if (byUsername != null && byUsername.getId() != currentUser.getId()) {
                chatGroup = service.getGroupByTwoUsers(byUsername.getId(), currentUser.getId());
                if (chatGroup != null) {
                    return BaseResponse.badRequest("Group between two user exist");
                }
            }
        }
        //create new chat group not exist
        chatGroup = new ChatGroup();
        chatGroup.setStatus(1);
        chatGroup.setName(request.getName());
        chatGroup = service.saveFlush(chatGroup);
        ChatGroupUser chatGroupUser = new ChatGroupUser();
        chatGroupUser.setUser(currentUser);
        chatGroupUser.setChatGroup(chatGroup);
        chatGroupUser.setStatus(1);
        chatGroupUser = chatGroupUserService.saveFlush(chatGroupUser);
        chatGroup.addChatGroupUser(chatGroupUser);
        for (String username :
                memberNames) {
            User user = userService.getByUsername(username.trim());
            if (user != null) {
                ChatGroupUser groupUser = new ChatGroupUser();
                groupUser.setUser(user);
                groupUser.setStatus(1);
                chatGroup.getUsers().add(groupUser);
                groupUser.setChatGroup(chatGroup);
                chatGroupUser.setChatGroup(chatGroup);
                groupUser = chatGroupUserService.saveFlush(groupUser);
                chatGroup.addChatGroupUser(groupUser);
            }
        }
        chatGroup.setName(request.getName());
        chatGroup = service.saveFlush(chatGroup);
        return BaseResponse.success(modelMapper.map(chatGroup, ChatGroupResponse.class), "create chat group success");
    }

    /**
     * lấy group chat giữa 2 user
     * TẠO ra group chat giữa 2 user nếu không tồn tại
     */
    public ResponseEntity<?> getChatGroupBetweenTwoUsers(Long userId) {
        User currentUser = userService.getCurrentUser();
        ChatGroup chatGroup = service.getGroupByTwoUsers(currentUser.getId(), userId);
        if (chatGroup == null) {
            log.info("tao chat group moi");
            chatGroup = new ChatGroup();
            chatGroup.setStatus(1);
            chatGroup = service.saveFlush(chatGroup);
            ChatGroupUser chatGroupUser = new ChatGroupUser();
            chatGroupUser.setUser(currentUser);
            chatGroupUser.setChatGroup(chatGroup);
            chatGroupUser.setStatus(1);
            User userTwo = userService.getById(userId);
            ChatGroupUser chatGroupUserTwo = new ChatGroupUser();
            chatGroupUserTwo.setUser(userTwo);
            chatGroupUserTwo.setStatus(1);
            chatGroupUserTwo.setChatGroup(chatGroup);
            chatGroupUser = chatGroupUserService.saveFlush(chatGroupUser);
            chatGroupUserTwo = chatGroupUserService.saveFlush(chatGroupUserTwo);
            chatGroup.getUsers().addAll(Arrays.asList(chatGroupUser, chatGroupUserTwo));
            chatGroup = service.saveFlush(chatGroup);
            return BaseResponse.success(modelMapper.map(chatGroup, ChatGroupResponse.class), "create chat group between two users success");
        }
        ChatGroupDetailResponse map = modelMapper.map(chatGroup, ChatGroupDetailResponse.class);
        log.info("chat group dât ton tai giua 2 user {} {} - {}", currentUser.getId(), userId, map);
        log.info("group id {}", chatGroup.getId());
        return BaseResponse.success(modelMapper.map(chatGroup, ChatGroupResponse.class), "get chat group between two users success");
    }

    public ResponseEntity<?> getChatGroupDetail(Long chatGroupId) {
        ChatGroup chatGroup = service.getById(chatGroupId);
        if (chatGroup != null) {
            ChatGroupDetailResponse data = modelMapper.map(chatGroup, ChatGroupDetailResponse.class);
            List<BaseUserResponse> baseUserResponses = new LinkedList<>();
            Set<ChatGroupUser> users = chatGroup.getUsers();
            for (ChatGroupUser user :
                    users) {
                BaseUserResponse baseUserResponse = modelMapper.map(user.getUser(), BaseUserResponse.class);
                baseUserResponses.add(baseUserResponse);
            }
            data.setUsers(baseUserResponses);
            return BaseResponse.success(data, "get chat group id " + chatGroupId + " detail success");
        }
        return BaseResponse.badRequest("Chat group with id: " + chatGroupId + " not exist");

    }

    public ResponseEntity<?> updateGroupInfo(Long groupId, UpdateGroupInfoRequest request) {
        ChatGroup group = service.getGroup(groupId, userService.getCurrentUser(), 1);
        if (group == null) {
            return BaseResponse.badRequest("Can not find group with id: " + groupId);
        }
        if (request.getName() != null) {
            group.setName(request.getName());
            service.save(group);
        }
        return BaseResponse.success("Update group info success!");
    }
}
