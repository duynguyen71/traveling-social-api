package com.tc.tcapi.service;

import com.tc.tcapi.model.Tour;
import com.tc.tcapi.model.User;
import com.tc.tcapi.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository repo;

    public void save(Tour tour) {
        repo.save(tour);
    }

    public Tour saveFlush(Tour tour) {
        return repo.save(tour);
    }

    public Tour getAvailableTour(Long tourId) {
        return null;
    }

    public boolean existByUserAndId(Long id, User user) {
        return repo.existsByIdAndUser(id, user);
    }

    /**
     * Get created tours
     *
     * @param user
     * @param pageable
     */
    public List<Tour> getCreatedTours(User user, Pageable pageable) {
        return repo.findByUserAndStatus(user, 1, pageable);
    }

    /**
     * Get tours
     *
     * @param user
     * @param status   0-rejected 1-requesting 2-approved
     * @param pageable
     */
    public List<Tour> getTours(User user, Integer status, Pageable pageable) {
        return repo.findByTourUsers_UserAndStatus(user, status, pageable);
    }

    public Tour getByOwnerAndTourUser(User owner, Long tourUserId) {
        return repo.findByUserAndTourUsers_Id(owner, tourUserId).orElse(null);
    }

    public Tour getCurrentJoinTour(User user) {
        List<Tour> tours = repo.getCurrentJoinTour(user.getId());
        if (tours.size() > 0) {
            return tours.get(0);
        }
        return null;
    }

    public Tour getByIdAndUser(Long id, User currentUser) {

        return repo.findByIdAndUser(id, currentUser).orElse(null);
    }
}
