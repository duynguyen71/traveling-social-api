package com.tc.tcapi.service;

import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.Message;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository repo;


    public Message getMessage(Long id, Integer status, User user) {
        return repo.findByIdAndStatusAndUser(id, status, user).orElse(null);
    }

    public Message getLastMessage(ChatGroup group, Integer status) {
        return repo.findFirstByChatGroupAndStatusOrderByCreateDateDesc(group, status).orElse(null);
    }

    public List<Message> getMessages(ChatGroup chatGroup, Integer status, Pageable pageable) {
        return repo.findByChatGroupAndStatus(chatGroup, status, pageable);
    }

    public Message getMessageInGroup(Long id, Integer status, ChatGroup chatGroup) {
        return repo.findByIdAndStatusAndChatGroup(id, status, chatGroup).orElse(null);
    }

    public Message saveFlush(Message message) {
        return repo.saveAndFlush(message);
    }

    public Message save(Message message) {
        return repo.save(message);
    }
}
