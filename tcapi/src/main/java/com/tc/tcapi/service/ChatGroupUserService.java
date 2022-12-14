package com.tc.tcapi.service;

import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.ChatGroupUser;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.ChatGroupUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGroupUserService {

    private final ChatGroupUserRepository repo;

    public ChatGroupUser saveFlush(ChatGroupUser user) {
        return repo.saveAndFlush(user);
    }

    public ChatGroupUser findByUserAndChatGroupId(User user,Long chatGroupId){
        return repo.findByUserAndChatGroup_Id(user,chatGroupId).orElse(null);
    }

    public List<ChatGroupUser> getChatUsers(ChatGroup chatGroup, Integer status) {
        return repo.findByChatGroupAndStatus(chatGroup, status);
    }

    public boolean existInGroup(ChatGroup chatGroup, User user) {
        return repo.existsByChatGroupAndUserAndStatus(chatGroup, user, 1);
    }

    public ChatGroupUser getChatGroupUser(Long chatUserId, Long currentUserId) {
        return repo.findByIdAndChatGroup_Users_User_Id(chatUserId, currentUserId).orElse(null);
    }


    public void save(ChatGroupUser chatGroupUser) {
        repo.save(chatGroupUser);
    }
}
