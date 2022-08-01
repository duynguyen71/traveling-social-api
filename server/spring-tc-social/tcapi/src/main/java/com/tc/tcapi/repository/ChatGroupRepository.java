package com.tc.tcapi.repository;

import com.tc.core.model.ChatGroup;
import com.tc.core.model.ChatGroupUser;
import com.tc.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {


    List<ChatGroup> findByUsersAndStatus(ChatGroupUser user, Integer status);

    List<ChatGroup> findByUsers_UserAndStatus(User user, Integer status);

    Optional<ChatGroup> findByIdAndUsers_UserAndStatus(Long id, User user, Integer i);

    Optional<ChatGroup> findByUsersInAndStatus(Collection<ChatGroupUser> users, Integer status);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT *, count(cgu.id)  FROM chat_group_user cgu\n" +
//                    "JOIN chat_group cg ON cg.id = cgu.chat_group_id \n" +
//                    "WHERE cgu.user_id IN (:id1, :id2) AND (SELECT COUNT(*) from chat_group_user subUCG\n" +
//                    "WHERE subUCG.chat_group_id = cgu.chat_group_id) = 2\n" +
//                    "group by cgu.chat_group_id\n" +
//                    "having count(cgu.id) = 2"
//    )
//    Optional<ChatGroup> findByTwoUser(@Param("id1") Long user1, @Param("id2") Long user2);

    @Query(
            nativeQuery = true,
            value = "SELECT cg.* FROM chat_group_user cgu\n" +
                    "JOIN chat_group cg ON cg.id = cgu.chat_group_id \n" +
                    "WHERE cgu.user_id IN (:id1, :id2) AND (SELECT COUNT(*) from chat_group_user subUCG\n" +
                    "WHERE subUCG.chat_group_id = cgu.chat_group_id) = 2\n" +
                    "group by cgu.chat_group_id\n" +
                    "having count(cgu.id) = 2"
    )
    Optional<ChatGroup> findByTwoUser(@Param("id1") Long user1, @Param("id2") Long user2);
}
