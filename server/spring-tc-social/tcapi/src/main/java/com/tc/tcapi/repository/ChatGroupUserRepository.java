package com.tc.tcapi.repository;

import com.tc.tcapi.model.ChatGroup;
import com.tc.tcapi.model.ChatGroupUser;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupUserRepository extends JpaRepository<ChatGroupUser,Long> {


     List<ChatGroupUser> findByChatGroupAndStatus(ChatGroup chatGroup,Integer status);

     boolean existsByChatGroupAndUserAndStatus(ChatGroup group,User user,Integer status);

     Optional<ChatGroupUser> findByIdAndChatGroup_Users_User_Id(Long chatGroupUserId, Long userId);


}
