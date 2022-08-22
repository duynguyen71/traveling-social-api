package com.tc.tcapi.service;

import com.tc.core.model.Follow;
import com.tc.core.model.User;
import com.tc.tcapi.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepo;

    public Follow saveAndFlush(Follow follow){
        return followRepo.saveAndFlush(follow);
    }

    public List<Follow> getFollowers(Long userId,Pageable pageable) {
        return followRepo.getFollowerNative(userId,pageable);
    }

    public List<Follow> getFollowingUsers(Long userId,Pageable pageable) {
        return followRepo.getFollowingNative(userId,pageable);

    }
    public int countFollowing(User user, int status) {
        return followRepo.countByFollowerAndStatus(user,status);
    }

    public int countFollower(User user, int status) {
        return followRepo.countByUserAndStatus(user,status);
    }

    public Follow getByUserAndFollower(User user,User follower){
        return followRepo.findByUserAndFollower(user, follower).orElse(null);
    }

    public boolean isFollowed(User user,User follower,Integer status){
        return  followRepo.existsByUserAndFollowerAndStatus(user,follower,status);
    }

}
