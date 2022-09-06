package com.tc.tcapi.service;

import com.tc.tcapi.model.Tour;
import com.tc.tcapi.model.TourUser;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.TourUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourUserService {

    private final TourUserRepository repo;

    /**
     * Count joined user
     * 0-rejected 1-requesting 2-approved
     */
    public int countJoinedUser(Tour tour) {
        return repo.countByTourAndStatus(tour, 2);
    }

    public int countRequestUser(Tour tour) {
        return repo.countByTourAndStatus(tour, 1);
    }

    public int countRejectedUser(Tour tour) {
        return repo.countByTourAndStatus(tour, 0);
    }

    public void save(TourUser tourUser) {
        repo.save(tourUser);
    }

    public TourUser getMemberOfMyTour(User currentUser, Long tourUserId) {
        return repo.findByTour_UserAndId(currentUser,tourUserId).orElse(null);
    }

    public List<TourUser> getTourUsers(Long tourId, User owner) {
        return repo.getTourUsersNative(owner.getId(), tourId);
    }

    public boolean availableTourUser(Long userId) {
        return repo.getJoinedTours(userId).isEmpty();
    }

    public List<TourUser> getApprovedUsersInTour(Long tourId, Long currentUserId) {
        return repo.getApprovedUserInTour(tourId, currentUserId);
    }
}
