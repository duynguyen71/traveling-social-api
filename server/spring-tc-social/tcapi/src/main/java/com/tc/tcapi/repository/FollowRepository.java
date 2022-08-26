package com.tc.tcapi.repository;

import com.tc.tcapi.model.Follow;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // count followers of user
    // xem so nguoi theo doi user
    int countByUserAndStatus(User user, int status);

    // count num of following of user
    // xem user theo doi bao nhiu nguoi
    int countByFollowerAndStatus(User user, int status);

    Optional<Follow> findByUserAndFollower(User user, User follower);

    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM user u \n" +
                    "JOIN follow f \n" +
                    "ON u.id = f.user_id\n" +
                    "WHERE u.id = :userId\n" +
                    "AND f.status = 1"
    )
    List<Follow> getFollowerNative(@Param("userId") Long userId, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT f.* FROM user u \n" +
                    "JOIN follow f \n" +
                    "ON u.id = f.follower_id\n" +
                    "WHERE u.id = :userId\n" +
                    "AND f.status = 1"
    )
    List<Follow> getFollowingNative(@Param("userId") Long userId, Pageable pageable);


    boolean existsByUserAndFollowerAndStatus(User user, User follower, Integer status);
}
