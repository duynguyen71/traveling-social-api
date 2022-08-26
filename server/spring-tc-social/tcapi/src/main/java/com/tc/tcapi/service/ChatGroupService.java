package com.tc.tcapi.service;

import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGroupService {

    private final ChatGroupRepository repo;

    public ChatGroup getById(Long id){
        return repo.findById(id).orElse(null);
    }

    //get user chat groups
    public List<ChatGroup> getGroups(User user, Integer status, Pageable pageable) {
        List<ChatGroup> groups = repo.findByUsers_UserAndStatus(user, status,pageable);
        return groups;
    }

    public ChatGroup getGroup(Long id, User user, Integer status) {
        return repo.findByIdAndUsers_UserAndStatus(id, user, 1).orElse(null);
    }

    public ChatGroup getGroupByTwoUsers(Long id1, Long id2) {
        return repo.findByTwoUser(id1, id2).orElse(null);
    }

    public ChatGroup saveFlush(ChatGroup chatGroup) {
        return repo.save(chatGroup);
    }

    public ChatGroup createChatGroup(ChatGroup request) {
        return repo.saveAndFlush(request);
    }

    public void save(ChatGroup chatGroup) {
        repo.save(chatGroup);
    }
}
