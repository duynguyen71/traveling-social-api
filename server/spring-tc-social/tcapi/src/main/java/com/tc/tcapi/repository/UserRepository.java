package com.tc.tcapi.repository;

import com.tc.core.model.Role;
import com.tc.core.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByVerificationCode(String code);

    List<User> findAllByRole(Role role);

    @Query(nativeQuery = true,
            value = "SELECT u.* FROM user u " +
                    "WHERE (:username IS NULL OR u.username LIKE :username) " +
                    "AND (:fullName IS NULL OR u.full_name LIKE :fullName) " +
                    "AND (:phone IS NULL OR u.phone LIKE :phone) " +
                    "AND (:email IS NULL OR u.email LIKE :email) " +
                    "AND (u.active = :active)")
    List<User> searchUsersNative(@Param("username") String username,
                                 @Param("fullName") String fullName,
                                 @Param("phone") String phone,
                                 @Param("email") String email,
                                 @Param("active") int active,
                                 Pageable pageable);


    Optional<User> findByUsernameOrEmail(String user, String email);

    Optional<User> findByIdAndActive(Long id, Integer active);

    @Query(value = "SELECT * FROM user  AS t1 JOIN (SELECT user_id, count(*) AS total \n" +
            "FROM(SELECT DISTINCT user_id FROM post\n" +
            "UNION ALL\n" +
            "SELECT user_id FROM review_post\n) AS counted\n" +
            "GROUP BY user_id \n" +
            "ORDER BY total DESC\n" +
            "LIMIT 10) AS t2 ON t1.id = t2.user_id\n", nativeQuery = true)
    List<User> getTopActiveUsers();
}
